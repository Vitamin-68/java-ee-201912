package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;
import ua.ithillel.dnepr.dml.service.FileEntitySerializer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class IndexedCrudRepositoryImpl<EntityType extends AbstractEntity<IdType>, IdType> implements IndexedCrudRepository {

    private final String rootDir;
    private Map<String, Integer> indexedField;
    private FileEntitySerializer fileEntitySerializer;
    private final boolean temporaryRepository;

    public IndexedCrudRepositoryImpl() {
        rootDir = System.getProperty("java.io.tmpdir") + "crudRoot/";
        indexedField = new HashMap<>();
        temporaryRepository = true;
        FileSystemSetup();
    }

    public IndexedCrudRepositoryImpl(Map<String, Integer> idx) {
        rootDir = System.getProperty("java.io.tmpdir") + "crudRoot/";
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

        return result;
    }

    @Override
    public void addIndex(String field) {

    }

    @Override
    public void addIndexes(List<String> fields) {

    }

    @Override
    public BaseEntity create(BaseEntity entity) {
        final AbstractEntity<IdType> tmpEntity = new AbstractEntity<IdType>() {
        };
        tmpEntity.setId((IdType) entity.getId());
        String fileName = tmpEntity.getUuid();
        if (!Files.exists(Paths.get(rootDir + fileName))) {
            serializeEntity(entity, fileName);
        }
        return entity;
    }

    private void serializeEntity(BaseEntity entity, String fileName) {
        final AbstractEntity<IdType> tmpEntity = new AbstractEntity<IdType>() {
        };
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
        //find file name

        //find all index files & delete records
        return null;
    }
}
