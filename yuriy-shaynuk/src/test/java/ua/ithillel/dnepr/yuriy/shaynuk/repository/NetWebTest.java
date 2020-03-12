package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class NetWebTest {

    @BeforeEach
    void setupClient(){

    }

    @Test
    void doGet() throws IOException {
        Gson gson = new Gson();
        URL url = new URL("http://localhost:8080/cities/4400");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());

        String response = readResponse(con);
        City responseCity = gson.fromJson(response, City.class);
        Assertions.assertEquals("Москва", responseCity.getName());
        con.disconnect();
    }

    @Test
    void doPost() throws IOException {
        City testCity = new City();
        testCity.setName("testName");
        testCity.setCountry_id(111);
        testCity.setRegion_id(222);
        testCity.setId(999);

        Gson gson = new Gson();
        String json = gson.toJson(testCity);

        URL url = new URL("http://localhost:8080/cities/4400");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.getOutputStream().write(json.getBytes());
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());
        con.disconnect();
    }

    @Test
    void doPut() {

    }

    @Test
    void doDelete() throws IOException {
        URL url = new URL("http://localhost:8080/cities/4400");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());
        con.disconnect();

        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, con.getResponseCode());
        con.disconnect();
    }

    private String readResponse(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }
}
