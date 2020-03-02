package ua.ithillel.dnepr.dml.domain.jpa;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends AbstractEntity<Integer> implements BaseEntity<Integer> {

    @Id
    @Column(unique = true)
    private Integer id;

    private String fName;

    private String lName;
}
