package vitaly.mosin.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Country extends AbstractEntity<Integer> {
    @Id
    private Integer id;
    private Integer cityId;
    private String name;

    public Country() {
    }

    public Country(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
