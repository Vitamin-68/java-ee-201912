package ua.ithillel.dnepr.roman.gizatulin.repository;

import java.io.Serializable;

public interface EntitySerializer<EntityType extends Serializable> {
    byte[] serialize(EntityType entity);

    EntityType deserialize(byte[] entity);
}
