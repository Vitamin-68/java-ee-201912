package ua.ithillel.dnepr.roman.gizatulin.repository.cqrs;

import ua.ithillel.dnepr.common.repository.cqrs.CqrsMutableRepository;
import ua.ithillel.dnepr.common.repository.cqrs.Observer;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class CqrsMutableRepositoryImpl<EntityType extends AbstractEntity<IdType>, IdType> implements CqrsMutableRepository<EntityType, IdType> {
    private final List<Observer<EntityType, IdType>> observers = new ArrayList<>();

    @Override
    public EntityType create(EntityType entity) {
        //TODO: Create entity
        sendNotification(entity);
        return null;
    }

    @Override
    public EntityType update(EntityType entity) {
        //TODO: Update entity
        sendNotification(entity);
        return null;
    }

    @Override
    public EntityType delete(IdType id) {
        //TODO: Delete entity
        EntityType result = null;
        sendNotification(result);
        return null;
    }

    @Override
    public void addListener(Observer<EntityType, IdType> observer) {
        observers.add(observer);
    }

    private void sendNotification(EntityType entity) {
        observers.forEach(observer -> {
            observer.update(entity);
        });
    }
}
