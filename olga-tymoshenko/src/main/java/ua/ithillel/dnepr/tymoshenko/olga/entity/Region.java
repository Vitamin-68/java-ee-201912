package ua.ithillel.dnepr.tymoshenko.olga.entity;
import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
@Setter
@Getter
public class Region extends AbstractEntity<Integer> {
    private Integer regionId;
    private Integer countryId;
    private Integer cityId;
    private String name;

}
