package ua.ithillel.alex.tsiba.repository.entity.jpa;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "cities")
public class City extends AbstractEntity<Integer> {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @ManyToOne (optional = false)
    @JoinColumn (name = "region_id")
    private Region region;

    @ManyToOne (optional = false)
    @JoinColumn (name = "country_id")
    private Country country;
}
