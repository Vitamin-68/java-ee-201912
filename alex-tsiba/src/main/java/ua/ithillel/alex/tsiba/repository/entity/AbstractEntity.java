package ua.ithillel.alex.tsiba.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.alex.tsiba.repository.annotations.Column;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

public abstract class AbstractEntity implements BaseEntity<Integer> {
    @Getter
    @Setter
    @Column(name = "id", isId = true, property = Column.ColumnProperty.AUTO_INCREMENT)
    private Integer id = 0;
}
