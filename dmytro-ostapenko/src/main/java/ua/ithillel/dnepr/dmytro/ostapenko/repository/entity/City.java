package ua.ithillel.dnepr.dmytro.ostapenko.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class City extends AbstractEntity<Integer> {
    private int cityId;
    private int countryId;
    private int regionId;
    private String cityName;
}
