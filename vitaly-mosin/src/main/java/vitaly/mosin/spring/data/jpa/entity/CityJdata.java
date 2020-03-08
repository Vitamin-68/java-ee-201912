package vitaly.mosin.spring.data.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "city")
public class CityJdata extends AbstractEntity<Integer> implements BaseEntity<Integer> {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryJdata country;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private RegionJdata region;

    private String name;

    public CityJdata() {
    }
}
