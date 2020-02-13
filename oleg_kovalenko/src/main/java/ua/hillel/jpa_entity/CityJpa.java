package ua.hillel.jpa_entity;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "City")
public class CityJpa extends AbstractEntity<Integer> {

    private int countryId;
    private int regionId;
    private String name;

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

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Column(name = "region_id")
    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
