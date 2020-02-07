package ua.ithillel.dnepr.dml.domain.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user")
public class User extends AbstractEntity<Integer> {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer Id;

    public void setId(Integer id){
        this.Id = id;
        super.setId(id);
    }

    private String fName;

    private String lName;
}
