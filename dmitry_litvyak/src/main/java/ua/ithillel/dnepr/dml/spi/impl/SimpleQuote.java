package ua.ithillel.dnepr.dml.spi.impl;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.dml.spi.Quote;

import java.util.Random;

@Slf4j
public class SimpleQuote implements Quote {
    @Override
    public void sayWisdom() {
        log.info("Your lucky number today is:" + (new Random().nextInt(1000)));
    }
}
