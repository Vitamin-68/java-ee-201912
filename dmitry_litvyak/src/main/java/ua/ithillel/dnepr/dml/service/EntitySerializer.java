package ua.ithillel.dnepr.dml.service;

import java.io.Serializable;

public interface EntitySerializer<EntityType extends Serializable> {
    String serialize(EntityType entity, String rootPath);

    EntityType deserialize(String path);
}
