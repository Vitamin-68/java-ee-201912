package repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

@Slf4j
public class MutableRepositoryImp<EntityType extends BaseEntity<IdType>, IdType>
        extends BaseFileRepository
        implements MutableRepository<EntityType, IdType> {

    public MutableRepositoryImp(String repoRootPath) {
        super(repoRootPath);
    }

    @Override
    public EntityType create(EntityType entity) {
        return null;
    }

    @Override
    public EntityType update(EntityType entity) {
        return null;
    }

    @Override
    public EntityType delete(IdType id) {
        return null;
    }
}
