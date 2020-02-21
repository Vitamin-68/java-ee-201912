package ua.ithillel.dnepr.tymoshenko.olga.entity;
import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

@Setter
@Getter
public class Region implements BaseEntity<Integer> {
    private Integer id;
    private Integer countryId;
    private Integer cityId;
    private String name;

    @Override
    public Integer getId() {
        return id;
    }
}
