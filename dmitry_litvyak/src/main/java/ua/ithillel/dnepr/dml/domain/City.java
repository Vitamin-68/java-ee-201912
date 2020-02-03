package ua.ithillel.dnepr.dml.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="city")
public class City extends AbstractEntity<Integer> {

    @ManyToOne(optional = false , cascade = CascadeType.DETACH)
    @JoinColumn(name = "id")
    private Integer country_id;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    private Integer region_id;

    private String name;
}
