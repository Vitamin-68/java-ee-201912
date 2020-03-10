package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "city")
public class City extends AbstractEntity<Integer> implements BaseEntity<Integer>{
    @Id
    @Column(unique = true, nullable = false)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="countryId")
    private Country country;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="regionId")
    private Region region;

    private String name;
}
