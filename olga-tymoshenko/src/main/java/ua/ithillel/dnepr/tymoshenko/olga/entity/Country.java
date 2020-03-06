package ua.ithillel.dnepr.tymoshenko.olga.entity;
import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Setter
@Getter
public class Country extends AbstractEntity<Integer> {
    private Integer countryd;
    private Integer cityId;
    private String name;
}
