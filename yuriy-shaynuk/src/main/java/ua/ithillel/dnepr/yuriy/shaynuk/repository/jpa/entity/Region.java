package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "region")
public class Region extends AbstractEntity<Integer> implements BaseEntity<Integer> {
    @Id
    @Column(unique = true, nullable = false)
    private Integer id;

    @ManyToMany
    private List<Country> country;

    @OneToOne(mappedBy="region")
    private City city;

    private String name;
}
