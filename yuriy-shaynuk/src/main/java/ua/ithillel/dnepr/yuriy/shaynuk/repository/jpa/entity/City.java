package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "city")
public class City  extends AbstractEntity<Integer> implements BaseEntity<Integer>{
    @Id
    private Integer id;
    private Integer countryId;
    private Integer regionId;
    private String name;
}
