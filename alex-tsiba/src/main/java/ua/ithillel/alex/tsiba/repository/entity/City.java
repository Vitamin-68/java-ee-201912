package ua.ithillel.alex.tsiba.repository.entity;

import lombok.Getter;
import lombok.Setter;
import ua.ithillel.alex.tsiba.repository.annotations.Column;
import ua.ithillel.alex.tsiba.repository.annotations.Table;

@Getter
@Setter
@Table(table = "city")
public class City extends AbstractEntity {
    @Column(name = "country_id", property = Column.ColumnProperty.INTEGER)
    private Integer countryId;
    @Column(name = "region_id", property = Column.ColumnProperty.INTEGER)
    private Integer regionId;
    @Column(name = "name")
    private String name;
}
