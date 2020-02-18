package ua.hillel.jpa_entity;

import lombok.*;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
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

    @OneToMany(mappedBy = "country", fetch = FetchType.EAGER)
    private List<CityJpa> cities;

    @Override
    public String toString() {
        return "CountryJpa{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                '}';
    }
}
