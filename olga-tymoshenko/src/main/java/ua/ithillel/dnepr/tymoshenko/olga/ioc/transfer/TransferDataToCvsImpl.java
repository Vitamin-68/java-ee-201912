package ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.tymoshenko.olga.jbcrepository.JdbcImmutableRepositoryImp;
import ua.ithillel.dnepr.tymoshenko.olga.util.EntityWorker;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransferDataToCvsImpl<EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements TransferData {

    private Connection connection;
    private String dest;

    @Override
    public void createConnection(String source, String dest) {
        this.dest = dest;
        UtilTransfer utilTransfer = new UtilTransfer();
        connection = utilTransfer.getBaseConnection(source);
    }

    @Override
    public void dataSending() {
        String[] headers;
        Object[] value;
        UtilTransfer utilTransfer = new UtilTransfer();
        Class<? extends AbstractEntity> clazz = utilTransfer.getClass(dest);
        try {
            JdbcImmutableRepositoryImp repository = new JdbcImmutableRepositoryImp(connection, clazz);
            Optional<List<EntityType>> listEntity = repository.findAll();
            if (listEntity.isPresent()) {
                for (EntityType entity : listEntity.get()) {
                    EntityWorker entityWorker = new EntityWorker(entity);
                    LinkedHashMap<String, Object> map = entityWorker.getFieldAndValue();
                    List<String> listHeader = new ArrayList<>();
                    List<Object> listValue = new ArrayList<>();
                    for (Map.Entry entry : map.entrySet()) {
                        listHeader.add((String) entry.getKey());
                        listValue.add(entry.getValue());
                    }
                    value = new Object[listValue.size() - 2];
                    for (int i = 0; i < value.length; i++) {
                        value[i] = listValue.get(i);
                    }
                    if (!Files.exists(new File(dest).toPath())) {
                        headers = new String[listHeader.size() - 2];
                        for (int i = 0; i < headers.length; i++) {
                            headers[i] = listHeader.get(i);
                        }
                        writeCSVFile(dest, false, headers, value);
                    } else {
                        writeCSVFile(dest, true, null, value);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            log.error(e.getMessage());
            throw new IllegalStateException(e);
        } finally {
            utilTransfer.closeConnection(connection);
        }
    }

    private void writeCSVFile(String pathFile, boolean append, String[] headers, Object[] value) throws IOException {
        char delimiter = ';';
        CSVPrinter csvPrinter;
        if (headers != null && headers.length != 0) {
            csvPrinter = new CSVPrinter(new FileWriter(pathFile, append), CSVFormat.DEFAULT.withHeader(headers).withDelimiter(delimiter));
        } else {
            csvPrinter = new CSVPrinter(new FileWriter(pathFile, append), CSVFormat.DEFAULT.withDelimiter(delimiter));
        }
        csvPrinter.printRecord(value);
        csvPrinter.close();
    }
}
