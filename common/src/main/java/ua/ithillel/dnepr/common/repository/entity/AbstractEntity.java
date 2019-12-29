package ua.ithillel.dnepr.common.repository.entity;

import java.util.UUID;

public abstract class AbstractEntity<IdType> implements BaseEntity<IdType> {
    private IdType id;
    private String uuid;

    @Override
    public IdType getId() {
        return id;
    }

    public void setId(IdType id) {
        this.id = id;
        uuid = UUID.nameUUIDFromBytes(String.valueOf(this.id).getBytes()).toString();
    }

    public String getUuid() {
        if (uuid == null) {
            uuid = UUID.nameUUIDFromBytes(String.valueOf(this.id).getBytes()).toString();
        }
        return this.uuid;
    }
}
