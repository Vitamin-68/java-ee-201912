package ua.hillel.jpa_entity;

import lombok.*;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Flow;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Country")
public class CountryJpa extends AbstractEntity<Integer> {

    @Id
    @Column(name = "country_id", unique = true)
    Integer id;

    private String name;

    @OneToMany(mappedBy = "country")
    private List<RegionJpa> region;

    @OneToMany(mappedBy = "country")
    private List<CityJpa> city;


}
