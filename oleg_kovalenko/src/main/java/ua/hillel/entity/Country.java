package ua.hillel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Country extends AbstractEntity<Integer> {
    int countryId;
    int cityId;
    String name;
}
