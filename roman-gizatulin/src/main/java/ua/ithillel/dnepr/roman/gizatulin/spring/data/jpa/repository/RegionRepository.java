package ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.entity.Region;

public interface RegionRepository extends PagingAndSortingRepository<Region, Integer> {
}
