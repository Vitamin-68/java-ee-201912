package vitaly.mosin.repository.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import vitaly.mosin.repository.entity.Country;
import vitaly.mosin.repository.entity.Region;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "city")
public class CityJpa extends AbstractEntity<Integer> {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    private Integer country;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id")
    private List<Region> regionList;

    private String name;

    public CityJpa() {
    }

//    public CityJpa(Integer id, Integer countryId, Integer regionId, String name) {
//        this.id = id;
//        this.countryId = countryId;
//        this.regionId = regionId;
//        this.name = name;
//    }
}

/*
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
        version="2.0">
<persistence-unit name="persistence-unit">
<provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
<class>vitaly.mosin.repository.jpa.entity.CityJpa</class>
<class>vitaly.mosin.repository.jpa.entity.RegionJpa</class>
<class>vitaly.mosin.repository.jpa.entity.CountryJpa</class>
<properties>
<!--            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver" />-->
<!--            <property name="javax.persistence.jdbc.url" value="jdbc:h2:file:./data;DB_CLOSE_DELAY=-1;MVCC=TRUE" />-->
<!--            <property name="javax.persistence.jdbc.user" value="sa" />-->
<!--            <property name="javax.persistence.jdbc.password" value="" />-->

<property name="hibernate.show_sql" value="true"/>
<property name="hibernate.hbm2ddl.auto" value="update"/>
<property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
<property name="hibernate.connection.url" value="jdbc:h2:file:./data"/>
</properties>
</persistence-unit>
</persistence>
*/