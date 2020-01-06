package ua.ithillel.dnepr.yevhen.lepet.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

@Getter
@Setter
public class Region implements BaseEntity<Integer> {
    private int id;
    private String name;

    @Override
    public Integer getId() {
        return id;
    }
}
