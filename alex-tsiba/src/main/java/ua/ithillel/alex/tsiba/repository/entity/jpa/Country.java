package ua.ithillel.alex.tsiba.repository.entity.jpa;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table(name = "countries")
public class Country extends AbstractEntity<Integer> {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private Collection<Region> regions;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private Collection<City> cities;
}
