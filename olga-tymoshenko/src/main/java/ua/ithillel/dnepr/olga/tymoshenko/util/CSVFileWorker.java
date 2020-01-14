package ua.ithillel.dnepr.olga.tymoshenko.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import ua.ithillel.dnepr.olga.tymoshenko.entity.City;
import ua.ithillel.dnepr.olga.tymoshenko.entity.Country;
import ua.ithillel.dnepr.olga.tymoshenko.entity.Region;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
@Getter
public class CSVFileWorker {
    private String fileName;
    private char delimiter;
    private CSVPrinter csvPrinter;

    public CSVFileWorker(String path) {

        if (path != null && !path.isEmpty())
            throw new IllegalArgumentException("File path is empty");
        delimiter = ';';
        this.fileName = path;
    }

    public CSVParser getCSVParser() {
        CSVParser csvParser = null;
        try {
            csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new FileReader(fileName));
        } catch (IOException e) {
            log.error("Failed to get parser", e);
        }
        return csvParser;
    }

    public boolean CSVWriteFile(boolean append, boolean withHeaders, Object object) {
        try {
            creatCSVPrinter(append, withHeaders);
            printDataIntoCSVFile(object);
            csvPrinter.close(true);
        } catch (IOException e) {
            log.error("Failed to write entity in file", e);
        }
        return true;
    }

    public boolean CSVWriteFile(boolean append, boolean withHeaders, List list) {
        try {
            creatCSVPrinter(append, withHeaders);
            for (Object object : list) {
                printDataIntoCSVFile(object);
            }
            csvPrinter.close(true);
        } catch (IOException e) {
            log.error("Failed to write list in file ", e);
        }
        return true;
    }

    private void creatCSVPrinter(boolean append, boolean withHeaders) throws IOException {
        String[] headers;
        if (withHeaders) {
            headers = getListHeaders();
            if (headers != null) {
                csvPrinter = new CSVPrinter(new FileWriter(fileName, append), CSVFormat.DEFAULT.withHeader(headers).withDelimiter(delimiter));
            }
        } else {
            csvPrinter = new CSVPrinter(new FileWriter(fileName, append), CSVFormat.DEFAULT.withDelimiter(delimiter));
        }
    }

    private void printDataIntoCSVFile(Object object) throws IOException {

        if (object instanceof City) {
            csvPrinter.printRecord(((City) object).getCityId(), ((City) object).getCountryId(), ((City) object).getRegionId(), ((City) object).getName());
        } else if (object instanceof Country) {
            csvPrinter.printRecord(((Country) object).getCountryId(), ((Country) object).getCityId(), ((Country) object).getName());
        } else if (object instanceof Region) {
            csvPrinter.printRecord(((Region) object).getRegionId(), ((Region) object).getCountryId(), ((Region) object).getCityId(), ((Region) object).getName());
        }

    }

    private String[] getListHeaders() throws IOException {
        String[] headers = null;
        List<String> listHeaders;
        CSVParser csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter(delimiter)
                .parse(new FileReader(fileName));
        listHeaders = csvParser.getHeaderNames();
        csvParser.close();
        if (listHeaders.size() != 0) {
            headers = new String[listHeaders.size()];

            for (int i = 0; i < listHeaders.size(); i++) {
                headers[i] = listHeaders.get(i);
            }
        }
        return headers;
    }
}
