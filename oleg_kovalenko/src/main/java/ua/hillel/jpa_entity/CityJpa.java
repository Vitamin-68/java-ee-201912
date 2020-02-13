package ua.hillel.jpa_entity;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "City")
public class CityJpa extends AbstractEntity<Integer> {

    private int countryId;
    private int regionId;
    private String name;

    private CountryJpa countryJpa;

    private RegionJpa regionJpa;

    public RegionJpa getRegionJpa() {
        return regionJpa;
    }

    public CountryJpa getCountryJpa() {
        return countryJpa;
    }

    @Override
    @Id
    @Column(name = "city_id")
    public Integer getId() {
        return super.getId();
    }

    @Column(name = "country_id")
    public int getCountryId() {
        return countryId;
    }

    @Column(name = "region_id")
    public int getRegionId() {
        return regionId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CityJpa{" + "city_id=" + getId() +
                ", countryId=" + countryId +
                ", regionId=" + regionId +
                ", name='" + name + '\'' +
                '}';
    }
}
