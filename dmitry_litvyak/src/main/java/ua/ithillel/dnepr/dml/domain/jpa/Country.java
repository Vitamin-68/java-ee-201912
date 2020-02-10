package ua.ithillel.dnepr.dml.domain.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "country")
public class Country extends AbstractEntity<Integer> implements BaseEntity<Integer> {

    @Id
    @Column(unique = true,nullable = false)
    Integer id;

    private String name;

    @OneToMany(mappedBy = "country")
    private Collection<Region> region;

    @OneToMany(mappedBy = "country")
    private Collection<City> city;


}
