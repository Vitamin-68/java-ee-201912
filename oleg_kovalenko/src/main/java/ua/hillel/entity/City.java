package ua.hillel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "City")
public class City extends AbstractEntity<Integer> {

    @Id
    @Column(name = "city_id")
    private int cityId;

    @Column(name = "country_id")
    private int countryId;

    @Column(name = "region_id")
    private int regionId;

    @Column(name = "name")
    private String name;
}
