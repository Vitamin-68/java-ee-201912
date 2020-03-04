package ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa.entity.City;

public interface CityRepository extends JpaRepository<City, Integer> {
}
