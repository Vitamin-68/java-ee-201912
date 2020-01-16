package ua.ithillel.alex.tsiba.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.alex.tsiba.repository.annotations.Column;
import ua.ithillel.alex.tsiba.repository.annotations.Table;

import java.util.ArrayList;
import java.util.List;

@Getter
@Table(table = "country")
public class Country extends AbstractEntity {
    @Setter
    @Column(name = "name")
    private String name;
    @Column(name = "city_id", property = Column.ColumnProperty.INTEGER)
    private Integer cityId = 0;
    private List<Integer> cityIds = new ArrayList<>();

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
        if (!this.cityIds.contains(cityId)) {
            this.cityIds.add(cityId);
        }
    }
}
