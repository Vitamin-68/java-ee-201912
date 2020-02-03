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
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer countryId;
    private Integer cityId;
    private String name;
}
