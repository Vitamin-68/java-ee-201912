package ua.hillel.jpa_entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "City")
public class CityJpa extends AbstractEntity<Integer> {

    @Id
    @Column(name = "city_id", unique = true)
    Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionJpa region;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryJpa country;
}
