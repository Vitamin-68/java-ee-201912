package ua.ithillel.dnepr.common.test.repository;

import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.roman.gizatulin.repository.EntitySerializer;
import ua.ithillel.dnepr.roman.gizatulin.repository.EntitySerializerImp;
import ua.ithillel.dnepr.roman.gizatulin.repository.IndexedCrudFileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class RomanGizatulinTestRepositoriesBuilder {
    public static List<CrudRepository<TestEntity, Integer>> buildRepositories() throws Exception {
        final List<CrudRepository<TestEntity, Integer>> result = new ArrayList<>();
        result.add(buildRomanGizatulinIndexedCrudFileRepository());
        return result;
    }

    private static CrudRepository<TestEntity, Integer> buildRomanGizatulinIndexedCrudFileRepository() throws IOException {
        String repoRootPath = Files.createTempDirectory("RomanGizatulinIndexedCrudFileRepository").toString();
        EntitySerializer<TestEntity> entitySerializer = new EntitySerializerImp<>();
        return new IndexedCrudFileRepository<>(repoRootPath, entitySerializer);
    }
}
