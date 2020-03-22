package servlet;

import com.google.gson.Gson;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

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
    static final int TYPE_CITY = 1;
    static final int TYPE_REGION = 2;
    static private CrudRepository<City, Integer> cityRepository;
    static private CrudRepository<Region, Integer> regionRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int type = Integer.parseInt(request.getParameterValues("type")[0]);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String json = null;
        if(type==TYPE_CITY) {
            Optional<List<City>> cityList = cityRepository.findAll();
            json = gson.toJson(cityList.get());
        }else if(type==TYPE_REGION){
            Optional<List<Region>> regionsList = regionRepository.findAll();
            json = gson.toJson(regionsList.get());
        }
        out.println(json);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void init() {
        File cityFile = Utils.createTempFile(getClass(),"city.csv");
        if (cityFile != null) {
            cityRepository = new CrudRepositoryImp<>(cityFile.getPath(),City.class);
        }
        File regionFile = Utils.createTempFile(getClass(),"region.csv");
        if (regionFile != null) {
            regionRepository = new CrudRepositoryImp<>(regionFile.getPath(),Region.class);
        }
    }
}
