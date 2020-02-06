package ua.ithillel.dnepr.dml.domain.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "user")
public class User extends AbstractEntity<Integer> {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long lId;

    private String fName;

    private String lName;
}
