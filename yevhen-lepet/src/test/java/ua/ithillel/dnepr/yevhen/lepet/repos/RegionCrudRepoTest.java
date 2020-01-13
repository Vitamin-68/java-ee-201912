package ua.ithillel.dnepr.yevhen.lepet.repos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.yevhen.lepet.entity.City;
import ua.ithillel.dnepr.yevhen.lepet.entity.Region;

import java.util.List;
import java.util.Optional;


@Slf4j
public class RegionCrudRepoTest {
    private RegionCrudRepo regionCrudRepo;
    private static final String PATH_REGION_CSV = "./src/main/resources/region.csv";

    @BeforeEach
    void setUp() {
        regionCrudRepo = new RegionCrudRepo(PATH_REGION_CSV);
    }

    @Test
    void findAll() {
        Assertions.assertNotNull(regionCrudRepo.findAll().get());
    }

    @Test
    void findById() {
        Optional<Region> testRegion = regionCrudRepo.findById(4312);
        Assertions.assertEquals(4312, testRegion.get().getId());
    }

    @Test
    void findByField(){
        Optional<List<Region>> regions = regionCrudRepo.findByField("name", "Адыгея");
        Assertions.assertTrue(regions.isPresent());
    }

    @Test
    void create(){
        Region testRegion = new Region();
        testRegion.setId(111212121);
        testRegion.setCountry_id(1231233);
        testRegion.setCity_id(12332);
        testRegion.setName("NewRegion");
        regionCrudRepo.update(testRegion);
        Optional <Region> result = regionCrudRepo.findById(111212121);
        Assertions.assertNotNull(result);
    }

    @Test
    void update(){
        Region testRegion = regionCrudRepo.findById(3352).get();
        testRegion.setName("NewRegion");
        regionCrudRepo.update(testRegion);
        Assertions.assertEquals(testRegion.getName(), regionCrudRepo.findById(3352).get().getName());
    }

    @Test
    void delete(){
        regionCrudRepo.delete(3630);
        Assertions.assertTrue(regionCrudRepo.findById(3630).isEmpty());
    }
}
