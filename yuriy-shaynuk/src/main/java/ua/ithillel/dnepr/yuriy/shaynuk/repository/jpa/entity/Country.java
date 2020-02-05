package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "country")
public class Country extends AbstractEntity<Integer> implements BaseEntity<Integer> {
    @Id
    @Column(unique = true, nullable = false)
    private Integer id;

    @OneToOne(mappedBy="country")
    private City city;

    private String name;
}
