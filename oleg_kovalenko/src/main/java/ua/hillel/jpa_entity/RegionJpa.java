package ua.hillel.jpa_entity;

import lombok.*;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Region")
public class RegionJpa extends AbstractEntity<Integer> {

    @Id
    @Column(name = "region_id", unique = true)
    Integer id;

    private String name;

    @OneToMany(mappedBy = "region")
    private List<CityJpa> city;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryJpa country;

}
