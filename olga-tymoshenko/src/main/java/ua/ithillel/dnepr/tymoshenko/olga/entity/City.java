package ua.ithillel.dnepr.tymoshenko.olga.entity;
import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
@Getter
@Setter
public class City extends AbstractEntity<Integer> {
    private Integer cityid;
    private Integer countryId;
    private Integer regionId;
    private String name;
}