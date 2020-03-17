package ua.ithillel.alex.tsiba.repository.entity.jpa;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table(name = "regions")
public class Region extends AbstractEntity<Integer> {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @OneToMany (mappedBy = "region", cascade = CascadeType.ALL)
    private Collection<City> cities;

    @ManyToOne (optional = false)
    @JoinColumn (name = "country_id")
    private Country country;
}
