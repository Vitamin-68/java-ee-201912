package vitaly.mosin.repository.csv;

import org.apache.commons.csv.CSVParser;

interface MyUtils {

    default int numberOfColumn4Seek(CSVParser csvParser, String fieldName) {
        for (int fieldNumber = 0; fieldNumber < csvParser.getHeaderNames().size(); fieldNumber++) {
            if (csvParser.getHeaderNames().get(fieldNumber).equalsIgnoreCase(fieldName)) {
                return fieldNumber;
            }
        }
        return -1;
    }
}
