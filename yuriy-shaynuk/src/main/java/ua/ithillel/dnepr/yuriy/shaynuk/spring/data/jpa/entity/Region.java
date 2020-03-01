package ua.ithillel.dnepr.yuriy.shaynuk.spring.data.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "region")
public class Region extends AbstractEntity<Integer> implements BaseEntity<Integer> {
    @Id
    private Integer id;
    private Integer countryId;
    private Integer cityId;
    private String name;
}
