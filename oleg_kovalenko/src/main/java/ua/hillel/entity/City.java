package ua.hillel.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class City extends AbstractEntity<Integer> {

    private int cityId;
    private int countryId;
    private int regionId;
    private String name;
}
