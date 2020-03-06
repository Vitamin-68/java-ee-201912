package ua.ithillel.dnepr.dml.spi.impl;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.dml.spi.Quote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
public class ChuckNorrisQoute implements Quote {

    private static final String CHUCK_URL = "https://api.chucknorris.io/jokes/random";
    private static final String CHUCK_UPSET = "Chuck is upset...";

    @Override
    public void sayWisdom() {
        try {
            URL url = new URL(CHUCK_URL);
            URLConnection conn = url.openConnection();
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = bf.readLine();
            if (result.contains("\"value\"")) {
                result = result.substring(result.lastIndexOf("value"), result.length());
                result = result.replace("value", "").replace("\"", "").replace("}", "").replace(":", "");
                log.info(result);
            }
        } catch (Exception e) {
            log.error(CHUCK_UPSET, e);
        }
    }
}
