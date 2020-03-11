import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.Characters;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/cities/*")
public class CitiesServlet extends HttpServlet {
    static File dataFile =null;
    static private CrudRepository<City, Integer> cityRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id;
        Optional<City> city = Optional.empty();
        if(!request.getRequestURI().equals("/cities")) {
            id = request.getRequestURI().replace("/cities/", "");
            city = cityRepository.findById(Integer.valueOf(id));
        }

        response.setContentType("text/html");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        if(city.isPresent()){
            out.println(city.get().getName());
        }else {
            out.println("<h3>Helloooo World!</h3>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        dataFile = Utils.createTempFile(getClass(),"city.csv");

        if (dataFile != null) {
            cityRepository = new CrudRepositoryImp<>(dataFile.getPath(),City.class);
        }
        super.init();
    }
}
