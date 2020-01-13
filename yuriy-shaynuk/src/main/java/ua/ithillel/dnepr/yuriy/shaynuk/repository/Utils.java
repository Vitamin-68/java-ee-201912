package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class Utils {
    public static final char delimiter = ';';

    public static File createTempFile(String resourcePath) {
        try {
            File tempFile = File.createTempFile("tmp", resourcePath);
            //tempFile.deleteOnExit();
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            if (in != null) {
                try (FileOutputStream out = new FileOutputStream(tempFile)) {
                    //copy stream
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }
            log.debug("tmp file path: "+tempFile.getAbsolutePath());
            return tempFile;
        } catch (IOException e) {
            log.error("getResourceAsFile exception",e);
            return null;
        }
    }

    public static CSVPrinter getPrinter(String repoRootPath){
        CSVPrinter csvPrinter = null;
        try {
            csvPrinter = new CSVPrinter(Files.newBufferedWriter(Path.of(repoRootPath), StandardOpenOption.APPEND),
                    CSVFormat.DEFAULT.withDelimiter(delimiter).withQuoteMode(QuoteMode.ALL));
        } catch (IOException e) {
            log.error("getPrinter exception",e);
        }
        return csvPrinter;
    }

    public static CSVParser getParser(String repoRootPath){
        CSVParser csvParser = null;
        try {
            csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(repoRootPath))));
        } catch (IOException e) {
            log.error("getParser exception",e);
        }
        return csvParser;
    }
}
