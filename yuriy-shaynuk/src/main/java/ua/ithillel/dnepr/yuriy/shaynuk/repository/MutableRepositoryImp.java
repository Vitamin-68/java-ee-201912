package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Slf4j
public class MutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseFileRepository<EntityType>
        implements MutableRepository<EntityType, IdType> {

    public MutableRepositoryImp(String repoRootPath, Class<EntityType> typeArgumentClass) {
        super(repoRootPath, typeArgumentClass);
    }

    @Override
    public EntityType create(EntityType entity) {
        return null;
    }

    @Override
    public EntityType update(EntityType entity) {
//        try {
//            CSVPrinter csvPrinter = new CSVPrinter(Files.newBufferedWriter(Path.of(repoRootPath), StandardOpenOption.APPEND),
//                    CSVFormat.DEFAULT.withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME).withDelimiter(delimiter));
//            for (City _region : allRecords.get()) {
//                csvPrinter.printRecord(_region.getId(), _region.getCountry_id(), _region.getRegion_id(), _region.getName());
//            }
//            csvPrinter.flush();
//            csvPrinter.close();
//        } catch (IOException e) {
//            log.error("CSV printer:", e);
//        }
        return null;
    }

    @Override
    public EntityType delete(IdType id) {
        return null;
    }
}
