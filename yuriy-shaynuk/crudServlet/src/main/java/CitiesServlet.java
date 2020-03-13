import com.google.gson.Gson;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/cities/*")
public class CitiesServlet extends HttpServlet {
    static private CrudRepository<City, Integer> cityRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        executeMethod(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        executeMethod(request,response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPut(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        executeMethod(request,response);
    }

    private void executeMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String id = request.getRequestURI().replace("/cities/", "");
        if (Utils.isInteger(id)) {
            int entityId = Integer.parseInt(id);
            switch (request.getMethod()){
                case HttpMethod.GET:{
                    Optional<City> city = cityRepository.findById(entityId);
                    if (city.isPresent()) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        String json = gson.toJson(city.get());
                        out.println(json);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    }
                }break;
                case HttpMethod.DELETE:{
                    City city = cityRepository.delete(entityId);
                    if (city != null) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        String json = gson.toJson(city);
                        out.println(json);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    }
                }break;
                case (HttpMethod.POST):{
                    String postBody = parseRequestBody(request.getInputStream());
                    City newCity = gson.fromJson(postBody, City.class);
                    newCity.setId(entityId);
                    if(cityRepository.findById(entityId).isPresent()){
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                    }else{
                        cityRepository.create(newCity);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }break;
                case HttpMethod.PUT:{
                    String postBody = parseRequestBody(request.getInputStream());
                    City newCity = gson.fromJson(postBody, City.class);
                    newCity.setId(entityId);
                    if(cityRepository.findById(entityId).isPresent()){
                        cityRepository.update(newCity);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }else{
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    }
                }break;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getOrDelete(HttpServletRequest request, HttpServletResponse response){

    }

    @Override
    public void init() {
        File dataFile = Utils.createTempFile(getClass(),"city.csv");
        if (dataFile != null) {
            cityRepository = new CrudRepositoryImp<>(dataFile.getPath(),City.class);
        }
    }

    private String parseRequestBody(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }
}
