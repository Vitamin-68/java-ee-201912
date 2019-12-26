package ua.ithillel.dnepr.roman.gizatulin.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Program02Test {

    @Test
    void add() {
        assertEquals(5, Program02.add(2, 3));
        assertEquals(5, Program02.add(3, 2));
    }
}