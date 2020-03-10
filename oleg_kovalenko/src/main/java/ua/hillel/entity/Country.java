package ua.hillel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Country extends AbstractEntity<Integer> {

    private int countryId;
    private int cityId;
    private String name;
}
