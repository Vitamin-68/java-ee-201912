package ua.ithillel.dnepr.roman.gizatulin.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Program01Test {
    private static Program01 testInstance;

    @BeforeAll
    static void setUp() {
        testInstance = new Program01();
    }

    @Test
    void add() {
        assertEquals(5, testInstance.add(2, 3));
        assertEquals(5, testInstance.add(3, 2));
    }
}