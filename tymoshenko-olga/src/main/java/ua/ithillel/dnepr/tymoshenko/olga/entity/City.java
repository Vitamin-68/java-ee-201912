package ua.ithillel.dnepr.tymoshenko.olga.entity;
import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

@Getter
@Setter
public class City implements BaseEntity<Integer> {
    private Integer id;
    private Integer countryId;
    private Integer regionId;
    private String name;

    @Override
    public Integer getId() {
        return id;
    }
}
