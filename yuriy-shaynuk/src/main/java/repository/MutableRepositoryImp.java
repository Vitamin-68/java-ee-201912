package repository;

import lombok.extern.slf4j.Slf4j;
import repository.entity.CsvEntity;
import ua.ithillel.dnepr.common.repository.MutableRepository;

@Slf4j
public class MutableRepositoryImp
        extends BaseFileRepository
        implements MutableRepository<CsvEntity, Integer> {

    public MutableRepositoryImp(String repoRootPath) {
        super(repoRootPath);
    }

    @Override
    public CsvEntity create(CsvEntity entity) {
        return null;
    }

    @Override
    public CsvEntity update(CsvEntity entity) {
        return null;
    }

    @Override
    public CsvEntity delete(Integer id) {
        return null;
    }
}
