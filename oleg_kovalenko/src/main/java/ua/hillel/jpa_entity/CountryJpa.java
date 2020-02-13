package ua.hillel.jpa_entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Flow;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "Country")
public class CountryJpa extends AbstractEntity<Integer> {

    private int cityId;
    private String name;

    @OneToOne
    @MapsId
    private RegionJpa regionJpa;

    @ManyToMany
    @JoinTable(name = "City",
            joinColumns = {@JoinColumn(name = "city_id", referencedColumnName = "city_id")},
            inverseJoinColumns = {@JoinColumn(name = "region_id", referencedColumnName = "region_id")})
    private Set<CityJpa> cities = new HashSet<>();

    public RegionJpa getRegionJpa() {
        return regionJpa;
    }

    @Override
    @Id
    @Column(name = "country_id")
    public Integer getId() {
        return super.getId();
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
        return "CountryJpa{" + "country_id=" + getId() +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                '}';
    }
}
