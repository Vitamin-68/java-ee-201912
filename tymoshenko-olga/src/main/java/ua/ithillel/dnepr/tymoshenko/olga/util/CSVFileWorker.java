package ua.ithillel.dnepr.tymoshenko.olga.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import ua.ithillel.dnepr.tymoshenko.olga.entity.City;
import ua.ithillel.dnepr.tymoshenko.olga.entity.Country;
import ua.ithillel.dnepr.tymoshenko.olga.entity.Region;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class CSVFileWorker {
    private String fileName;
    private char delimiter;
    private CSVPrinter csvPrinter;
    @Getter
    private CSVParser csvParser;
    @Getter
    private List<String> listHeaders;

    public CSVFileWorker(String path, char delimiter) throws IOException {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }
        this.delimiter = delimiter;
        this.fileName = path;

        csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withDelimiter(delimiter)
                .parse(new FileReader(fileName, StandardCharsets.UTF_8));

        listHeaders = csvParser.getHeaderNames();
    }

    public void CSVWriteFile(boolean append, boolean withHeaders, Object object) throws IOException {
        creatCSVPrinter(append, withHeaders);
        printDataIntoCSVFile(object);
        csvPrinter.close(true);
    }

    public void CSVWriteFile(boolean append, boolean withHeaders, List list) throws IOException {
        creatCSVPrinter(append, withHeaders);
        for (Object object : list) {
            printDataIntoCSVFile(object);
        }
        csvPrinter.close(true);
    }

    private void creatCSVPrinter(boolean append, boolean withHeaders) throws IOException {
        String[] headers;
        if (withHeaders) {
            if (!listHeaders.isEmpty()) {
                headers = new String[listHeaders.size()];
                for (int i = 0; i < headers.length; i++) {
                    headers[i] = listHeaders.get(i);
                }
                csvPrinter = new CSVPrinter(new FileWriter(fileName, append), CSVFormat.DEFAULT.withHeader(headers).withDelimiter(delimiter));
            }
        } else {
            csvPrinter = new CSVPrinter(new FileWriter(fileName, append), CSVFormat.DEFAULT.withDelimiter(delimiter));
        }
    }

    private void printDataIntoCSVFile(Object object) throws IOException {
        String[] arr = null;
        String cityID;
        String countryID;
        String regionID;
        String nameEntity;
        if (object instanceof City) {
            cityID = String.valueOf(((City) object).getId());
            countryID = String.valueOf(((City) object).getCountryId());
            regionID = String.valueOf(((City) object).getRegionId());
            nameEntity = ((City) object).getName();
            arr = new String[]{cityID, countryID, regionID, nameEntity};
        } else if (object instanceof Country) {
            cityID = String.valueOf(((Country) object).getCityId());
            countryID = String.valueOf(((Country) object).getId());
            nameEntity = ((Country) object).getName();
            arr = new String[]{countryID, cityID, nameEntity};
        } else if (object instanceof Region) {
            cityID = String.valueOf(((Region) object).getCityId());
            countryID = String.valueOf(((Region) object).getCountryId());
            regionID = String.valueOf(((Region) object).getId());
            nameEntity = ((Region) object).getName();
            arr = new String[]{regionID, countryID, cityID, nameEntity};
        }
        if (arr != null)
            csvPrinter.printRecord(arr);
     }
}


