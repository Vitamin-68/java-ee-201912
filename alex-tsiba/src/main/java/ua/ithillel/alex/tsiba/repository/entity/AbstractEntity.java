package ua.ithillel.alex.tsiba.repository.entity;

import lombok.Getter;
import ua.ithillel.alex.tsiba.repository.annotations.Column;

import java.util.UUID;

public abstract class AbstractEntity extends ua.ithillel.dnepr.common.repository.entity.AbstractEntity<Integer> {
    @Getter
    @Column(name = "id", isId = true, property = Column.ColumnProperty.AUTO_INCREMENT)
    private Integer id = -1;
    private String uuid;

    public void setId(Integer id) {
        this.id = id;
        uuid = UUID.nameUUIDFromBytes(String.valueOf(this.id).getBytes()).toString();
    }
}
