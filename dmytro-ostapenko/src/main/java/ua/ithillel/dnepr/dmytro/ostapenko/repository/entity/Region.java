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
public class Region extends AbstractEntity<Integer> {
    private int regionId;
    private int countryId;
    private int cityId;
    private String regionName;
}
