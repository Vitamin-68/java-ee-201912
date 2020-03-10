package ua.ithillel.dnepr.dml.spi.impl;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.dml.spi.Quote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Random;

@Slf4j
public class FortuneCookieQoute implements Quote {

    public static final String NON_LUCKY = "All cookies was eaten! Unfortunately....";
    public static final int MAX_COUNT = 100;

    @Override
    public void sayWisdom() {
        int lineNumber = new Random().nextInt(MAX_COUNT);
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL resource = classLoader.getResource("FortuneCookie.txt");
        if (resource == null) {
            log.info(NON_LUCKY);
        } else {
            File fl = new File(resource.getFile());
            try {
                FileReader fr = new FileReader(fl);
                BufferedReader stringReader = new BufferedReader(fr);
                log.info(stringReader.lines().skip(Math.max(0, lineNumber - 1)).findFirst().get());
            } catch (Exception e) {
                log.error(NON_LUCKY, e);
            }
        }
    }
}
