package ua.ithillel.dnepr.common.repository.cqrs;

import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

public interface Observable<EntityType extends AbstractEntity<IdType>, IdType> {
    void addListener(Observer<EntityType, IdType> observer);
}
