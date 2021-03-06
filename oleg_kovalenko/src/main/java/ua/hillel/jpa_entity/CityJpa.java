package ua.hillel.jpa_entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "City")
public class CityJpa extends AbstractEntity<Integer> {

    @Id
    @Column(name = "city_id", unique = true)
    Integer id;

    @Column(name = "country_id")
    private int countryId;

    @Column(name = "region_id")
    private int regionId;

    @Column(name = "name")
    private String name;

    public CityJpa(Integer id, int countryId, int regionId, String name) {
        this.id = id;
        this.countryId = countryId;
        this.regionId = regionId;
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id", nullable = false, insertable = false, updatable = false)
    private RegionJpa region;


    @Override
    public String toString() {
        return String.format("CityJpa: city_id %d, country_id %d, region_id %d, name %s, countru %s",
                id, countryId, regionId, name, region.getName());
    }
}
