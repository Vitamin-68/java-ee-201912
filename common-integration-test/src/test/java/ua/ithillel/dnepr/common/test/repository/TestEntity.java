package ua.ithillel.dnepr.common.test.repository;

import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

public class TestEntity extends AbstractEntity<Integer> {
    private String filed1;
    private String filed2;
    private int field3;

    public String getFiled1() {
        return filed1;
    }

    public void setField1(String filed1) {
        this.filed1 = filed1;
    }

    public String getFiled2() {
        return filed2;
    }

    public void setField2(String filed2) {
        this.filed2 = filed2;
    }

    public int getField3() {
        return field3;
    }

    public void setField3(int field3) {
        this.field3 = field3;
    }
}
