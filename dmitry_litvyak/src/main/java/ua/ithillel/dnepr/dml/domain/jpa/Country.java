package ua.ithillel.dnepr.dml.domain.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Country extends AbstractEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Integer Id;
    public void setId(Integer id){
        this.Id = id;
        super.setId(id);
    }

    private String name;

    @OneToMany(mappedBy = "country",orphanRemoval = true)
    private Collection<Region> region = new ArrayList<Region>();

    @OneToMany(mappedBy = "country",orphanRemoval = true)
    private Collection<City> city = new ArrayList<City>();


}
