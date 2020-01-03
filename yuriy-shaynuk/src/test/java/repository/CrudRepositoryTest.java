package repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CrudRepositoryTest {
    File countryFile;
    File cityFile;
    File regionFile;

    @BeforeEach
    void setUp() {
        countryFile = getResourceAsFile("city.csv");
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByField() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    private File getResourceAsFile(String resourcePath) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            if (in == null) {
                return null;
            }

            File tempFile = File.createTempFile(resourcePath, ".csv");
            //tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            System.out.println(tempFile.getAbsolutePath());
            return tempFile;
        } catch (IOException e) {
            log.error("getResourceAsFile exception",e);
            return null;
        }
    }
}