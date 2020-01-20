package ua.ithillel.dnepr.olga.tymoshenko.repositiry;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.olga.tymoshenko.entity.Region;
import ua.ithillel.dnepr.olga.tymoshenko.util.CSVFileWorker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegionRepository implements CrudRepository<Region, Integer> {

    private final CSVFileWorker fileWorker;

    public RegionRepository(File file) {
        fileWorker = new CSVFileWorker(file.getPath());
    }

    @Override
    public Optional<List<Region>> findAll() {
        Optional<List<Region>> result;
        final List<Region> regions = new ArrayList<>();

        final CSVParser csvParser = fileWorker.getCSVParser();
        try {
            for (CSVRecord record : csvParser.getRecords()) {

                regions.add(setRecordIntoRegion(record));
            }
            csvParser.close();
        } catch (IOException e) {
            //     e.printStackTrace();
        }
        result = Optional.of(regions);
        return result;
    }

    @Override
    public Optional<Region> findById(Integer id) {
        Optional<Region> result = Optional.empty();
        Region region = new Region();

        final CSVParser csvParser = fileWorker.getCSVParser();
        try {
            for (CSVRecord record : csvParser.getRecords()) {
                if (Integer.parseInt(record.get(0)) == id) {
                    region = setRecordIntoRegion(record);

                }
                csvParser.close();
            }
            result = Optional.of(region);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        Optional<List<Region>> result = Optional.empty();
        final List<Region> regions = new ArrayList<>();

        final CSVParser csvParser = fileWorker.getCSVParser();
        try {
            List<String> header = csvParser.getHeaderNames();
            if (header.contains(fieldName)) {

                for (CSVRecord record : csvParser.getRecords()) {
                    if (record.get(fieldName).equals(value.toString()))
                        regions.add(setRecordIntoRegion(record));
                }
            }
            csvParser.close();
            result = Optional.of(regions);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Region create(Region entity) {
        Optional<Region> city = findById(entity.getCountryId());

        if (city.isPresent()) {
            if (city.get().getCountryId() != null) {
                return new Region();
            }
        }

        if (fileWorker.CSVWriteFile(true, false, entity)) {
            return entity;
        }
        return new Region();
    }

    @Override
    public Region update(Region entity) {
        Optional<Region> region = findById(entity.getRegionId());
        if (region.isPresent()) {
            if (region.get().getCityId() == null) {
                return new Region();
            }
        }

        Optional<List<Region>> allRegion = findAll();

        if (allRegion.isPresent()) {
            for (Region record : allRegion.get()) {
                if (record.getRegionId().equals(entity.getRegionId())) {
                    record.setCityId(entity.getCityId());
                    record.setCountryId(entity.getCountryId());
                    record.setName(entity.getName());

                }
            }


            if (fileWorker.CSVWriteFile(false, true, allRegion.get())) {
                return entity;
            }
        }

        return new Region();
    }

    @Override
    public Region delete(Integer id) {
        Optional<Region> region = findById(id);
        if (region.isPresent()) {
            if (region.get().getCityId() == null) {
                return region.get();
            }
        }
        Optional<List<Region>> allRegion = findAll();
        Region delRegion = null;

        if (allRegion.isPresent()) {
            for (Region record : allRegion.get()) {
                if (record.getRegionId().equals(id)) {
                    delRegion = record;
                    allRegion.get().remove(allRegion.get().indexOf(record));
                    break;
                }
            }


            if (fileWorker.CSVWriteFile(false, true, allRegion.get())) {
                return delRegion;
            }
        }
        return new Region();
    }

    private Region setRecordIntoRegion(CSVRecord record) {
        final Region region = new Region();
        region.setRegionId(Integer.parseInt(record.get(0)));
        region.setCountryId(Integer.parseInt(record.get(1)));
        region.setCityId(Integer.parseInt(record.get(2)));
        region.setName(record.get(3));
        return region;

    }
}
