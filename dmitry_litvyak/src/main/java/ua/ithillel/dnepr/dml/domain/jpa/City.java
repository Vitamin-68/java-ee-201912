package ua.ithillel.dnepr.dml.domain.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="city")
public class City {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    Long Id;

    @ManyToOne(optional = false , cascade = CascadeType.DETACH)
    @JoinColumn(name = "id")
    private Long country_id;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    private Long region_id;

    private String name;
}
