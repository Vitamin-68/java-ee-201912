package ua.ithillel.dnepr.roman.gizatulin.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.BaseEntity;

@Getter
@Setter
public class City implements BaseEntity<Integer> {
    private int id;
    private String name;

    @Override
    public Integer getId() {
        return id;
    }
}
