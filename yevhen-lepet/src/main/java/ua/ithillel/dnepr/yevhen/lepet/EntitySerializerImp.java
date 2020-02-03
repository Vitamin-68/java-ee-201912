package ua.ithillel.dnepr.yevhen.lepet;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

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
    @SuppressWarnings("unchecked")
    public EntityType deserialize(byte[] entity) {
        EntityType result;
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(entity);
        try (final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            result = (EntityType) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            log.error("Failed to read entity", e);
            throw new IllegalArgumentException("Failed to read entity", e);
        }
        return result;
    }
}
