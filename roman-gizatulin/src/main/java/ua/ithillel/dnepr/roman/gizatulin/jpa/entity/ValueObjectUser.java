package ua.ithillel.dnepr.roman.gizatulin.jpa.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ValueObjectUser {
    private String fName;
    private String lName;
    private int age;

    public static ValueObjectUser of(String fName, String lName, int age) {
        return new ValueObjectUser(fName, lName, age);
    }

    private ValueObjectUser(String fName, String lName, int age) {
        this.fName = fName;
        this.lName = lName;
        this.age = age;
    }
}
