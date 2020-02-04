package ua.ithillel.dnepr.eugenekovalov.repository.crud;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class MutableRepositoryImpl<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseCrudRepo<EntityType> implements MutableRepository<EntityType, IdType> {

    protected MutableRepositoryImpl(Path path, Class<EntityType> entityTypeClass) {
        super(path, entityTypeClass);
    }

    @Override
    public EntityType create(EntityType entity) {
        Optional<List<EntityType>> optionalEntityType = getAllRecords();
        List<EntityType> entities = new ArrayList<>();

        if (optionalEntityType.isPresent()) {
            entities = optionalEntityType.get();
        }
        entities.add(entity);
        writeCsv(entities, delimiter);
        return entity;
    }

    @Override
    public EntityType update(EntityType entity) {
        List<EntityType> entities = getAllRecords()
                .stream()
                .flatMap(List::stream)
                .map(e -> e.getId().equals(entity.getId()) ? entity : e)
                .collect(Collectors.toList());
        writeCsv(entities, delimiter);
        return entity;
    }

    @Override
    public EntityType delete(IdType id) {
        List<EntityType> entities = entitiesList(getAllRecords());
        EntityType entity = null;
        for (EntityType entityType: entities) {
            if (entityType.getId().equals(id)) {
                entity = entityType;
                entities.remove(entity);
                break;
            }
        }
        if (entity == null) {
            log.warn("Entity not found with: ", id);
            return null;
        }
        writeCsv(entities, delimiter);
        return entity;
    }

    private void writeCsv(List<EntityType> entities, char delimiter) {
        CSVParser csvParser = null;
        CSVPrinter csvPrinter = null;
        List<String> headers = null;
        try {
            csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new FileReader(path.toFile()));
        } catch (IOException e) {
            log.error("Parsing error", e);
        }

        headers = csvParser.getHeaderNames();
        if (headers.isEmpty()) {
            headers = formHeaders();
        }

        try {
            csvPrinter = new CSVPrinter(new FileWriter(path.toFile()),
                    CSVFormat.DEFAULT
                    .withDelimiter(delimiter).withQuoteMode(QuoteMode.ALL));
        } catch (IOException e) {
            log.error("Writing error", e);
        }

        try {
            csvPrinter.printRecord(headers);
            for (EntityType entity : entities) {
                csvPrinter.print(entity.getId().toString());
                for (int i = 1; i < headers.size(); i++) {
                    Field field = entity.getClass().getDeclaredField(headers.get(i));
                    field.setAccessible(true);
                    csvPrinter.print(field.get(entity).toString());
                }
                csvPrinter.println();
            }
            csvPrinter.close(true);
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            log.error("Operation error", e);
        }
    }
}
