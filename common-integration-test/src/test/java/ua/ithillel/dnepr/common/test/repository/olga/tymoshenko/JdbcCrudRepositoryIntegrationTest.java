package ua.ithillel.dnepr.common.test.repository.olga.tymoshenko;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.test.repository.TestEntity;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.tymoshenko.olga.jbcrepository.JdbcCrudRepository;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;

import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateManyNewEntities;
import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateOneNewEntity;

@Slf4j
public class JdbcCrudRepositoryIntegrationTest {
    private static final String TEST_DB_NAME = "test_db";
    private static final H2Server H2_SERVER = new H2Server();

    private JdbcCrudRepository<TestEntity, Integer> crudRepository;
    private String repoRootPath;
    Path tmpDirectory;


    @BeforeEach
    void setup() throws IOException, SQLException {
        tmpDirectory = Files.createTempDirectory("olgaTymoshenko");
        repoRootPath = Path.of(tmpDirectory.toString(), TEST_DB_NAME).toString();
        log.info("Database path: {}", repoRootPath);
        final Connection connection = H2_SERVER.getConnection(repoRootPath);
        log.info("Server start");
        crudRepository = new JdbcCrudRepository<>(connection, TestEntity.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        H2_SERVER.stop();
        log.info("Server stoped");
        deleteFolder(tmpDirectory);
    }

    @Test
    void createOneNewEntity() {
        testCreateOneNewEntity(crudRepository);
    }

    @Test
    void createManyNewEntities() {
        testCreateManyNewEntities(crudRepository);
    }

    private void deleteFolder(final Path folder) throws IOException {
        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;
                }
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
