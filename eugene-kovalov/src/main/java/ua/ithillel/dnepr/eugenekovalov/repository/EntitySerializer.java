package ua.ithillel.dnepr.eugenekovalov.repository;

import java.io.Serializable;

public interface EntitySerializer<EntityType extends Serializable> {
    byte[] serialize(EntityType entity);

    EntityType deserialize(byte[] entity);
}
