package ua.ithillel.dnepr.dml.service;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class FileEntitySerializer<EntityType extends Serializable> implements EntitySerializer {
    @Override
    public String serialize(Serializable entity, String path) {
        try {
            path = path;
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(path));
            objectOutputStream.writeObject(entity);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e) {
            log.error("Serialization:", e);
        }
        return path;
    }

    @Override
    public EntityType deserialize(String path) {
        EntityType result;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
            result = (EntityType) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            log.error("Deserialization:", e);
            throw new IllegalArgumentException("Failed to read entity", e);
        }
        return result;
    }
}
