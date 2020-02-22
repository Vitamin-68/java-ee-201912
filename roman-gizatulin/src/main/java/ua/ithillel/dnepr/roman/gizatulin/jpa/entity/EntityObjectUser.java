package ua.ithillel.dnepr.roman.gizatulin.jpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user")
public class EntityObjectUser {
    @Id
    @GeneratedValue
    private String id;

    private String fName;
    private String lName;
    private int age;
}
