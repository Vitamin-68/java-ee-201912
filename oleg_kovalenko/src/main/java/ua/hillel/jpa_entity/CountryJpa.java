package ua.hillel.jpa_entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Country")
public class CountryJpa extends AbstractEntity<Integer> {

    private int cityId;
    private String name;

    @Override
    @Id
    @Column(name = "country_id")
    public Integer getId() {
        return super.getId();
    }

    @Column(name = "city_id")
    public int getCityId() {
        return cityId;
    }


    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CountryJpa{" + "country_id=" + getId() +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                '}';
    }
}
