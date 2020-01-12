package ua.ithillel.dnepr.eugenekovalov.repository;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.IOException;


@Slf4j
public class EntitySerializerImp<EntityType extends Serializable> implements EntitySerializer<EntityType> {
    @Override
    public byte[] serialize(EntityType entity) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(entity);
        } catch (IOException e) {
            log.error("Failed to write entity", e);
            throw new IllegalArgumentException("Failed to write entity", e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public EntityType deserialize(byte[] entity) {
        EntityType result;
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(entity);
        try (final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            result = (EntityType) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed to read entity", e);
            throw new IllegalArgumentException("Failed to read entity", e);
        }
        return result;
    }
}
