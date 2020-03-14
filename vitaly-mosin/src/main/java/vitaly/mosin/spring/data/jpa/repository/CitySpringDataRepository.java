package vitaly.mosin.spring.data.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import vitaly.mosin.spring.data.jpa.entity.CityJdata;

import java.util.List;
import java.util.Optional;

public interface CitySpringDataRepository extends CrudRepository<CityJdata, Integer> {
    Optional<List<CityJdata>> findByNameAndNameEquals(String fieldName, String value);

//    Optional<List<CityJpa>> findByName();


    //    @Override
//    public Optional<List<CityJpa>> findAll();
//
//    @Override
//    public Optional<CityJpa> findById(Integer id) {
//        return Optional.empty();
//    }
//
//    @Override
//    public Optional<List<CityJpa>> findByField(String fieldName, Object value) {
//        return Optional.empty();
//    }
//
//    @Override
//    public CityJpa create(CityJpa entity) {
//        return null;
//    }
//
//    @Override
//    public CityJpa update(CityJpa entity) {
//        return null;
//    }
//
//    @Override
//    public CityJpa delete(Integer id) {
//        return null;
}
