package ua.hillel.jpa_entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "Region")
public class RegionJpa extends AbstractEntity<Integer> {
    private int countryId;
    private int cityId;
    private String name;

    @OneToOne(mappedBy = "country_id", cascade = CascadeType.ALL)
    private CountryJpa countryJpa;

    @ManyToMany(mappedBy = "Region")
    private Set<CountryJpa> countries = new HashSet<>();

    @Override
    @Id
    @Column(name = "region_id")
    public Integer getId() {
        return super.getId();
    }

    @Column(name = "country_id")
    public int getCountryId() {
        return countryId;
    }

    @Column(name = "city_id")
    public int getCityId() {
        return cityId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "RegionJpa{" + "region_id=" + getId() +
                ", countryId=" + countryId +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                '}';
    }
}
