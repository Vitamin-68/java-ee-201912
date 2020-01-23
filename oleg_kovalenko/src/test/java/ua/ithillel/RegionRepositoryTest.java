package ua.ithillel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.hillel.csvRepo.CrudRepositoryRegion;
import ua.hillel.entity.Region;
import ua.ithillel.dnepr.common.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
public class RegionRepositoryTest {

    private CrudRepository repositoryRead;
    private CrudRepository repositoryDml;
    String pathForRead = "src/test/resources/region.csv";
    String pathForDml = "src/test/resources/regionTest.csv";
    char delimeter = ';';

    @BeforeEach
    void init() {
        repositoryRead = new CrudRepositoryRegion(pathForRead, delimeter);
        repositoryDml = new CrudRepositoryRegion(pathForDml, delimeter);
    }

    @Test
    public void findAllTest() {
        Assertions.assertNotNull(repositoryRead.findAll());
    }

    @Test
    public void findByIdTest() {
        Optional<Region> expected = repositoryRead.findById(3468);
        int actual = 3468;
        Assertions.assertEquals(actual, expected.get().getRegionId());
    }


    @Test
    public void findByFieldCountriIdTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("countryId", "3159");
        int actual = 78;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByFieldRegionIdTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("regionId", "3407");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void findByNameTest() {
        Optional<List<Region>> expected = repositoryRead.findByField("name", "Бурятия");
        int actual = 1;
        Assertions.assertEquals(actual, expected.get().size());
    }

    @Test
    public void createTest() {
        repositoryDml.create(new Region(1852456, 12365, 212, "Просто регион"));
        Optional<Region> region = repositoryDml.findById(1852456);
        int expected = region.get().getRegionId();
        int actual = 1852456;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void updateNameTest() {
        repositoryDml.update(new Region(5052, 3159, 0, "Сахалин Туц-Туц"));
        Optional<Region> region = repositoryDml.findById(5052);
        String expected = region.get().getName();
        String actual = "Сахалин Туц-Туц";
        Assertions.assertEquals(actual, expected);
    }
}