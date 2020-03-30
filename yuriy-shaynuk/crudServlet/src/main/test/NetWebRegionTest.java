import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class NetWebRegionTest {

    @Test
    void doGet() throws IOException {
        Gson gson = new Gson();
        URL url = new URL("http://localhost:8080/regions/3407");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());

        String response = readResponse(con);
        Region responseCity = gson.fromJson(response, Region.class);
        Assertions.assertEquals("Бурятия", responseCity.getName());
        con.disconnect();
    }

    @Test
    void doPost() throws IOException {
        Region testRegion = new Region();
        testRegion.setName("testName");
        testRegion.setCountry_id(111);
        testRegion.setCity_id(222);

        Gson gson = new Gson();
        String json = gson.toJson(testRegion);

        URL url = new URL("http://localhost:8080/regions/9999");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.getOutputStream().write(json.getBytes());
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());
        con.disconnect();
    }

    @Test
    void doPut() throws IOException {
        Region testRegion = new Region();
        testRegion.setName("testName");
        testRegion.setCountry_id(1111);
        testRegion.setCity_id(2222);

        Gson gson = new Gson();
        String json = gson.toJson(testRegion);

        URL url = new URL("http://localhost:8080/regions/3630");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setDoOutput(true);
        con.getOutputStream().write(json.getBytes());
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, con.getResponseCode());
        con.disconnect();
    }

    @Test
    void doDelete() throws IOException {
        URL url = new URL("http://localhost:8080/regions/3160");
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
