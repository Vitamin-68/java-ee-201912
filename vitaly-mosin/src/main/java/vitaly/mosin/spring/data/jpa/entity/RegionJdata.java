package vitaly.mosin.spring.data.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "region")
public class RegionJdata {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    private Integer countryId;
    private Integer cityId;
    private String name;
}
