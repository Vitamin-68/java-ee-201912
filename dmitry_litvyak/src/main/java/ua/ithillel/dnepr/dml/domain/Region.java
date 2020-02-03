package ua.ithillel.dnepr.dml.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "region")
public class Region extends AbstractEntity<Integer> {

    @ManyToOne
    @JoinColumn(name = "id")
    private Integer country_id;

    @OneToMany(mappedBy = "id")
    private Integer city_id;

    private String name;
}
