package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;
import ua.ithillel.dnepr.dml.service.FileEntitySerializer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class IndexedCrudRepositoryImpl<EntityType extends AbstractEntity<IdType>, IdType> implements IndexedCrudRepository {

    private final String rootDir;
    private final Map<String, Integer> indexedField;
    private FileEntitySerializer fileEntitySerializer;

    public IndexedCrudRepositoryImpl() {
        rootDir = System.getProperty("java.io.tmpdir") + "crudRoot/";
        indexedField = new HashMap<>();
        FileSystemSetup();
    }

    private void FileSystemSetup() {
        fileEntitySerializer = new FileEntitySerializer();
        if (!Files.exists(Paths.get(rootDir))) {
            try {
                Files.createDirectory(Paths.get(rootDir));
            } catch (Exception e) {
                log.error("Create root dir:", e);
            }
        }
    }

    public IndexedCrudRepositoryImpl(Map<String, Integer> idx) {
        rootDir = System.getProperty("java.io.tmpdir") + "crudRoot/";
        indexedField = idx;
        FileSystemSetup();
    }

    public IndexedCrudRepositoryImpl(String path) {
        rootDir = path;
        indexedField = new HashMap<>();
        FileSystemSetup();
    }

    public IndexedCrudRepositoryImpl(String path, Map<String, Integer> idx) {
        rootDir = path;
        indexedField = idx;
        FileSystemSetup();
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        Optional<List<EntityType>> result = Optional.empty();

        return result;
    }

    @Override
    public Optional findById(Object id) {
        return Optional.empty();
    }

    @Override
    public Optional<List> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public void addIndex(String field) {

    }

    @Override
    public void addIndexes(List<String> fields) {

    }

    @Override
    public BaseEntity create(BaseEntity entity) {
        //serialize entity & save
        final AbstractEntity<IdType> tmpEntity = new AbstractEntity<IdType>() {
        };
        tmpEntity.setId((IdType) entity.getId());
        String fileName = tmpEntity.getUuid();
        fileEntitySerializer.serialize(entity, rootDir + fileName);
        //create or update data in index file
        indexedField.forEach((indexName, v) -> {
            try {
                Method getter = entity.getClass().getDeclaredMethod("get" + indexName);
                String idxFileValue = getter.invoke(entity).toString();
                tmpEntity.setId((IdType) idxFileValue);
                String idxUuid = tmpEntity.getUuid();
                String idxPartPath = idxUuid.substring(0, 2) + '/' + idxUuid.substring(2, 4) + '/' + idxUuid;
                saveIndex(rootDir + indexName + '/' + idxPartPath, entity, indexName, fileName);
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
        return entity;
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
        return null;
    }

    @Override
    public BaseEntity delete(Object id) {
        return null;
    }
}
