package repository;

import lombok.extern.slf4j.Slf4j;
import repository.entity.CsvEntity;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CrudRepository implements ua.ithillel.dnepr.common.repository.CrudRepository<CsvEntity, Integer> {
    private String filePath;
    private final ImmutableRepository<CsvEntity, Integer> immutableRepository;
    private final MutableRepository<CsvEntity, Integer> mutableRepository;

    public CrudRepository(String filePath) {
        this.filePath = filePath;
        immutableRepository = new ImmutableRepositoryImp(filePath);
        mutableRepository = new MutableRepositoryImp(filePath);
    }

    @Override
    public Optional<List<CsvEntity>> findAll() {
        return immutableRepository.findAll();
    }

    @Override
    public Optional<CsvEntity> findById(Integer id) {
        return immutableRepository.findById(id);
    }

    @Override
    public Optional<List<CsvEntity>> findByField(String fieldName, Object value) {
        return immutableRepository.findByField(fieldName,value);
    }

    @Override
    public CsvEntity create(CsvEntity entity) {
        return mutableRepository.create(entity);
    }

    @Override
    public CsvEntity update(CsvEntity entity) {
        return mutableRepository.update(entity);
    }

    @Override
    public CsvEntity delete(Integer id) {
        return mutableRepository.delete(id);
    }
}
