package ua.hillel.jpa_entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Country")
public class CountryJpa extends AbstractEntity<Integer> {

    @Id
    @Column(name = "country_id", unique = true)
    private Integer id;

    @Column(name = "city_id")
    private int cityId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "country")
    private List<RegionJpa> region;

    public CountryJpa(Integer id, int cityId, String name) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("CountryJpa: country_id %d, city_id %d, name %s",
                id, cityId, name);
    }
}
