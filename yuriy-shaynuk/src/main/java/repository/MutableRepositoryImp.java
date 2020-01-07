package repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import repository.entity.CsvEntity;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
public class MutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseFileRepository
        implements MutableRepository<EntityType, IdType> {

    public MutableRepositoryImp(String repoRootPath) {
        super(repoRootPath);
    }

    @Override
    public EntityType create(EntityType entity) {
        return null;
    }

    @Override
    public EntityType update(EntityType entity) {
        try {
            CSVPrinter csvPrinter = new CSVPrinter(Files.newBufferedWriter(Path.of(repoRootPath), StandardOpenOption.APPEND),
                    CSVFormat.DEFAULT.withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME).withDelimiter(delimiter));
            for (City _region : allRecords.get()) {
                csvPrinter.printRecord(_region.getId(), _region.getCountry_id(), _region.getRegion_id(), _region.getName());
            }
            csvPrinter.flush();
            csvPrinter.close();
        } catch (IOException e) {
            log.error("CSV printer:", e);
        }
        return null;
    }

    @Override
    public EntityType delete(IdType id) {
        return null;
    }
}
