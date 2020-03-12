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

    @Override
    public void destroy() {
        super.destroy();
    }

    private void executeMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        String id = request.getRequestURI().replace("/cities/", "");
        if (Utils.isInteger(id)) {
            switch (request.getMethod()){
                case HttpMethod.GET:{
                    Optional<City> city = cityRepository.findById(Integer.valueOf(id));
                    if (city.isPresent()) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        Gson gson = new Gson();
                        String json = gson.toJson(city.get());
                        out.println(json);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    }
                }break;
                case HttpMethod.DELETE:{
                    City city = cityRepository.delete(Integer.valueOf(id));
                    if (city != null) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        Gson gson = new Gson();
                        String json = gson.toJson(city);
                        out.println(json);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    }
                }break;
                case HttpMethod.POST:{
                    String postBody = parseRequest(request.getInputStream());
                    out.println("123");
                }break;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void init() throws ServletException {
        File dataFile = Utils.createTempFile(getClass(),"city.csv");

        if (dataFile != null) {
            cityRepository = new CrudRepositoryImp<>(dataFile.getPath(),City.class);
        }
        super.init();
    }

    private String parseRequest(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }
}
