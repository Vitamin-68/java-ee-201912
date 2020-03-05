package ua.ithillel.dnepr.dml.domain.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "region")
public class Region extends AbstractEntity<Integer> implements BaseEntity<Integer> {

    @Id
    @Column(unique = true)
    Integer id;

    private String name;

    @OneToMany(mappedBy = "region")
    private List<City> city = new ArrayList<City>();

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
}
