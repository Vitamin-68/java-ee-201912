/**
 * @author Oleg Kovalenko
 */


package ua.hillel.jpa_entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Region")
public class RegionJpa extends AbstractEntity<Integer> {

    @Id
    @Column(name = "region_id", unique = true)
    private Integer id;

    @Column(name = "country_id")
    private int countryId;

    @Column(name = "city_id")
    private int cityId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<CityJpa> cities;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id", nullable = false, insertable = false, updatable = false)
    private CountryJpa country;

    public RegionJpa(Integer id, int countryId, int cityId, String name) {
        this.id = id;
        this.countryId = countryId;
        this.cityId = cityId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "RegionJpa{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                ", country=" + country +
                '}';
    }
}