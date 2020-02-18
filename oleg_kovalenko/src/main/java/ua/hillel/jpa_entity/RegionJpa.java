/**
 * @author Oleg Kovalenko
 */


package ua.hillel.jpa_entity;

import lombok.*;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Region")
public class RegionJpa extends AbstractEntity<Integer> {

    @Id
    @Column(name = "region_id", unique = true)
    private Integer id;

    @Column(name = "country_id")
    private int countryId;

    @Column(name = "city_id")
    private int cityId;

    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return "RegionJpa{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                '}';
    }
}