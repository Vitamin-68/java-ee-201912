import com.google.gson.Gson;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@WebServlet("/getList")
public class CrudServlet extends HttpServlet {
    static private CrudRepository<City, Integer> crudRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Optional<List<City>>cityList = crudRepository.findAll();
        String json = gson.toJson(cityList);
        out.println(json);
        response.setStatus(HttpServletResponse.SC_OK);
//        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

    @Override
    public void init() {
        File dataFile = Utils.createTempFile(getClass(),"city.csv");
        if (dataFile != null) {
            crudRepository = new CrudRepositoryImp<>(dataFile.getPath(),City.class);
        }
    }
}
