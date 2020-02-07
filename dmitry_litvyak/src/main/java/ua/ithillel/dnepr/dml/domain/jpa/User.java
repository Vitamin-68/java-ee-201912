package ua.ithillel.dnepr.dml.domain.jpa;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user")
public class User extends AbstractEntity<Integer> implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private Integer Id;

    public void setId(Integer id) {
        this.Id = id;
        super.setId(id);
    }

    private String fName;

    private String lName;
}
