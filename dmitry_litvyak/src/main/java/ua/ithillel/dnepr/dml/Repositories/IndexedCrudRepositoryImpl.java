package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;
import ua.ithillel.dnepr.dml.service.FileEntitySerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class IndexedCrudRepositoryImpl<EntityType extends AbstractEntity<IdType>, IdType> implements IndexedCrudRepository {

    private final String rootDir;
    private Map<String, Integer> indexedField;
    private FileEntitySerializer fileEntitySerializer;
    private final boolean temporaryRepository;

    public IndexedCrudRepositoryImpl() {
        rootDir = System.getProperty("java.io.tmpdir") + "crudRoot"+File.separator;
        indexedField = new HashMap<>();
        temporaryRepository = true;
        FileSystemSetup();
    }

    public IndexedCrudRepositoryImpl(Map<String, Integer> idx) {
        rootDir = System.getProperty("java.io.tmpdir") + "crudRoot"+File.separator;
        indexedField = idx;
        temporaryRepository = true;
        FileSystemSetup();
    }

    public IndexedCrudRepositoryImpl(String path) {
        rootDir = path + File.separator;
        indexedField = new HashMap<>();
        temporaryRepository = false;
        FileSystemSetup();
    }

    public IndexedCrudRepositoryImpl(String path, Map<String, Integer> idx) {
        rootDir = path + File.separator;
        indexedField = idx;
        temporaryRepository = false;
        FileSystemSetup();
    }

    private void FileSystemSetup() {
        fileEntitySerializer = new FileEntitySerializer();
        if (!Files.exists(Paths.get(rootDir))) {
            try {
                Path dirPath = Files.createDirectory(Paths.get(rootDir));
                if(temporaryRepository){
                    dirPath.toFile().deleteOnExit();
                }
            } catch (Exception e) {
                log.error("Create root dir:", e);
            }
        }
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        Optional<List<EntityType>> result = Optional.empty();
        try {
            Stream<Path> serializedObjects = Files.find(Paths.get(rootDir), 1, (path, attrs) -> {
                return !attrs.isDirectory();
            });
            List<EntityType> entityList = new ArrayList<EntityType>();
            serializedObjects.forEach(fileObject->{
                EntityType enity = (EntityType) fileEntitySerializer.deserialize(fileObject.toAbsolutePath().toString());
                entityList.add(enity);
            });
            if(!entityList.isEmpty()) {
                result = Optional.of(entityList);
            }
        }catch (Exception e){
            log.error("No files found",e);
        }
        return result;
    }

    @Override
    public Optional findById(Object id) {
        Optional<EntityType> result = Optional.empty();
        AbstractEntity<IdType> tmpEntity = new AbstractEntity<>() {};
        tmpEntity.setId((IdType) id);
        String fileName = tmpEntity.getUuid();
        try {
            if(Files.exists(Paths.get(rootDir+fileName))){
                EntityType entity = (EntityType) fileEntitySerializer.deserialize(rootDir+fileName);
                result = Optional.of(entity);
            }
        }catch (Exception e){
            log.error("No enity found",e);
        }
        return  result;
    }

    @Override
    public Optional<List> findByField(String fieldName, Object value) {
        Optional<List> result = Optional.empty();
        //1. Id field
        if(fieldName.equals("Id")){
            result = findById(value);
        }
        //2. indexed field
        else if(indexedField.containsKey(fieldName)){
            List<EntityType> tmpList = new ArrayList<>();
            final AbstractEntity<IdType> tmpEntity = new AbstractEntity<IdType>() { };
            tmpEntity.setId((IdType) value);
            String idxFileName = rootDir + File.separator
                    + fieldName + File.separator
                    + tmpEntity.getUuid().substring(0,2) + File.separator
                    + tmpEntity.getUuid().substring(2,4) + File.separator
                    + tmpEntity.getUuid();
            if(Files.exists(Paths.get(idxFileName))){
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(idxFileName));
                    String line;
                    while ((line = bufferedReader.readLine())!=null){
                        EntityType object = (EntityType) fileEntitySerializer.deserialize(rootDir+line);
                            tmpList.add(object);
                    }
                    if(!tmpList.isEmpty()){
                        result = Optional.of(tmpList);
                    }
                }catch(Exception e){
                    log.error("Index read error",e);
                }
            }
        }
        else {
        //3. not indexed field
            List<EntityType> tmpList = new ArrayList<EntityType>();
            Optional allItems = findAll();
            if(allItems.isPresent()){
                for (EntityType obj:(ArrayList<EntityType>)allItems.get()) {
                    try {
                        Method method = obj.getClass().getDeclaredMethod("get"+fieldName);
                        if(Objects.equals(method.invoke(obj),value)){
                            tmpList.add(obj);
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        log.error("Field name exception",e);
                    }
                }
                if(!tmpList.isEmpty()){
                    result = Optional.of(tmpList);
                }
            }
        }
        return result;
    }

    @Override
    public void addIndex(String field) {
        List<String> indexArray = new ArrayList<>();
        indexArray.add(field);
        addIndexes(indexArray);
    }

    @Override
    public void addIndexes(List<String> fields) {
        Optional<List<EntityType>> allEntities = findAll();
        final AbstractEntity<IdType> tmpEntity = new AbstractEntity<IdType>() {};
        Method getter;
        for (String newField : fields) {
            try {
                if (allEntities.isPresent()) {
                    for (EntityType entity : allEntities.get()) {
                        getter = entity.getClass().getDeclaredMethod("get" + newField);
                        if (!indexedField.containsKey(newField)) {
                            indexedField.put(newField, indexedField.size() + 1);
                        }
                        String idxFileValue = getter.invoke(entity).toString();
                        tmpEntity.setId((IdType) idxFileValue);
                        String idxUuid = tmpEntity.getUuid();
                        String idxPartPath = idxUuid.substring(0, 2) + File.separator + idxUuid.substring(2, 4) + File.separator + idxUuid;
                        saveIndex(rootDir + newField + File.separator + idxPartPath, entity, newField, entity.getUuid());
                    }
                }else{
                    log.warn("No one entities, can't add new index");
                }
            } catch (Exception e) {
                log.error("No field to index", e);
            }
        }
    }

    @Override
    public BaseEntity create(BaseEntity entity) {
        final AbstractEntity<IdType> tmpEntity = new AbstractEntity<IdType>() { };
        tmpEntity.setId((IdType) entity.getId());
        String fileName = tmpEntity.getUuid();
        if (!Files.exists(Paths.get(rootDir + fileName))) {
            serializeEntity(entity, fileName);
        }
        return entity;
    }

    private void serializeEntity(BaseEntity entity, String fileName) {
        final AbstractEntity<IdType> tmpEntity = new AbstractEntity<IdType>() {};
        fileEntitySerializer.serialize(entity, rootDir + fileName);
        //create or update data in index file
        indexedField.forEach((indexName, v) -> {
            try {
                Method getter = entity.getClass().getDeclaredMethod("get" + indexName);
                String idxFileValue = getter.invoke(entity).toString();
                tmpEntity.setId((IdType) idxFileValue);
                String idxUuid = tmpEntity.getUuid();
                String idxPartPath = idxUuid.substring(0, 2) + File.separator + idxUuid.substring(2, 4) + File.separator + idxUuid;
                saveIndex(rootDir + indexName + File.separator + idxPartPath, entity, indexName, fileName);
            } catch (IOException e) {
                log.error("Index create:", e);
            } catch (NoSuchMethodException e) {
                log.error("Index create(no method found):", e);
            } catch (IllegalAccessException e) {
                log.error("Index create(no access to method):", e);
            } catch (InvocationTargetException e) {
                log.error("Index create(invok target):", e);
            }
        });
    }

    private void saveIndex(String idxFile, BaseEntity entity, String fieldName, String uuid) throws IOException {
        Path idxPath = Paths.get(idxFile);
        if (!Files.exists(idxPath)) {
            Files.createDirectories(idxPath.getParent());
            Files.createFile(Paths.get(idxFile));
        }
        BufferedReader txtReader = new BufferedReader(new FileReader(idxFile));
        boolean isPresent = false;
        String fileLine;
        while ((fileLine = txtReader.readLine()) != null) {
            if (fileLine.contains(uuid)) isPresent = true;
        }
        txtReader.close();
        if (!isPresent) {
            FileWriter fileWriter = new FileWriter(idxFile, true);
            fileWriter.append(uuid + '\n');
            fileWriter.flush();
            fileWriter.close();
        }
    }

    @Override
    public BaseEntity update(BaseEntity entity) {
        final AbstractEntity<IdType> tmpEntity = new AbstractEntity<IdType>() {        };
        tmpEntity.setId((IdType) entity.getId());
        String fileName = tmpEntity.getUuid();
        serializeEntity(entity, fileName);
        return entity;
    }

    @Override
    public BaseEntity delete(Object id) {
        BaseEntity result = null;
        final AbstractEntity<IdType> tmpEntity = new AbstractEntity<IdType>() {
        };
        //find file name
        Optional<EntityType> entity = findById(id);
        if (entity.isPresent()) {
            //find all index files & delete records
            tmpEntity.setId((IdType) id);
            String mainFile = tmpEntity.getUuid();
            indexedField.forEach((indexName, v) -> {
                try {
                    Method getter = entity.get().getClass().getDeclaredMethod("get" + indexName);
                    String idxFileValue = getter.invoke(entity.get()).toString();
                    tmpEntity.setId((IdType) idxFileValue);
                    String idxUuid = tmpEntity.getUuid();
                    String idxPartPath = idxUuid.substring(0, 2) + File.separator + idxUuid.substring(2, 4) + File.separator + idxUuid;
                    String fullPath = rootDir + indexName + File.separator + idxPartPath;
                    if(Files.exists(Paths.get(fullPath))){
                        Path tmpFile = Files.createTempFile(Paths.get(rootDir), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_ss")),null);
                        Files.copy(Paths.get(fullPath),tmpFile, StandardCopyOption.REPLACE_EXISTING);
                        BufferedReader txtReader = new BufferedReader(new FileReader(tmpFile.toFile()));
                        FileWriter fileWriter = new FileWriter(fullPath, false);
                        String fileLine;
                        while ((fileLine = txtReader.readLine()) != null) {
                            if (!fileLine.contains(mainFile)) {
                                fileWriter.write(fileLine + '\n');
                            }
                        }
                        txtReader.close();
                        fileWriter.flush();
                        fileWriter.close();
                        Files.deleteIfExists(tmpFile);
                    }
                } catch (Exception e) {
                    log.error("delete index", e);
                }
            });
            result = tmpEntity;
            try {
                Files.deleteIfExists(Paths.get(rootDir + mainFile));
            } catch (IOException e) {
                log.error("delete main entity",e);
            }
        }
        return result;
    }
}
