package ua.ithillel.dnepr.yevhen.lepet.repos;

import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yevhen.lepet.entity.Region;

import java.util.List;
import java.util.Optional;

public class RegionCrudRepo implements CrudRepository<Region, Integer> {
    @Override
    public Optional<List<Region>> findAll() {
        return Optional.empty();
    }

    @Override
    public Optional<Region> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public Region create(Region entity) {
        return null;
    }

    @Override
    public Region update(Region entity) {
        return null;
    }

    @Override
    public Region delete(Integer id) {
        return null;
    }
}
