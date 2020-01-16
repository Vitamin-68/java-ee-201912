package ua.ithillel.alex.tsiba.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.alex.tsiba.repository.annotations.Column;
import ua.ithillel.alex.tsiba.repository.annotations.Table;

import java.util.ArrayList;
import java.util.List;

@Getter
@Table(table = "region")
public class Region extends AbstractEntity {
    @Setter
    @Column(name = "name")
    private String name;
    @Setter
    @Column(name = "country_id", property = Column.ColumnProperty.INTEGER)
    private Integer countryId;
    @Column(name = "city_id", property = Column.ColumnProperty.INTEGER)
    private Integer cityId;
    private List<Integer> cityIds = new ArrayList<>();

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
        if (!this.cityIds.contains(cityId)) {
            this.cityIds.add(cityId);
        }
    }
}
