package ua.hillel.jpa_entity;


import lombok.*;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
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



    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "country_id", nullable = false, insertable = false, updatable = false)
    private CountryJpa country;

    @Override
    public String toString() {
        return "CityJpa{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", regionId=" + regionId +
                ", name='" + name + '\'' +
                ", country=" + country +
                '}';
    }
}
