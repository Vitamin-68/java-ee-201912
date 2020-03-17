package ua.ithillel.dml.web;

import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.dml.Repositories.CityRepository;
import ua.ithillel.dnepr.dml.Repositories.CountryRepository;
import ua.ithillel.dnepr.dml.Repositories.RegionRepository;
import ua.ithillel.dnepr.dml.domain.City;
import ua.ithillel.dnepr.dml.domain.Country;
import ua.ithillel.dnepr.dml.domain.Region;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@WebServlet({"/cities/*","/regions/*","/countries/*"})
public class CityServlet extends HttpServlet {

    public static final String ERROR_RESPONSE = "<H1>hello world</H1>";
    private CityRepository cityRepository;
    private RegionRepository regionRepository;
    private CountryRepository countryRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String idValue = getIdFromRequest(req);
        if (idValue.isEmpty()) {
            writer.println(ERROR_RESPONSE);
        } else {
            Optional<? extends AbstractEntity<Integer>> obj = (Optional<? extends AbstractEntity<Integer>>) getCrudRepository(req.getHttpServletMapping().getPattern()).findById(Integer.parseInt(idValue));
            if (obj.isPresent()) {
                writer.println("<H2>ID:"+obj.get().getId().toString()+"</H2>");
                try{
                Method method = obj.get().getClass().getDeclaredMethod("getName");
                writer.println("name:"+method.invoke(obj));
                }catch (Exception e){
                    throw new ServletException(e);
                }
            } else {
                writer.println("<H1> no object with this id</H1>");
            }
        }
    }

    private CrudRepository<? extends AbstractEntity<Integer>,Integer> getCrudRepository(String pattern) {
        if (pattern.contains("cities")) {
            return cityRepository;
        }else if (pattern.contains("regions")) {
            return regionRepository;
        }else if (pattern.contains("countries")) {
            return countryRepository;
        }else {
            return null;
        }
    }

    private String getIdFromRequest(HttpServletRequest req) {
        String idString = req.getHttpServletMapping().getMatchValue();
        String idStringValue = "";
        for (char c : idString.toCharArray()) {
            if (new String("1234567890").contains(String.valueOf(c))) {
                idStringValue += (String.valueOf(c));
            }else break;
        }
        return idStringValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String idValue = getIdFromRequest(req);
        if (idValue.isEmpty()) {
            writer.println(ERROR_RESPONSE);
        } else {
            if (getCrudRepository(req.getHttpServletMapping().getPattern()).findById(Integer.parseInt(idValue)).isEmpty()) {

                getCrudRepository(req.getHttpServletMapping().getPattern()).create(getRepoObject(req, idValue));
            } else {
                getCrudRepository(req.getHttpServletMapping().getPattern()).update(getRepoObject(req, idValue));
            }
        }
        resp.getWriter().println("MAYBE CREATED!");
    }

    private <T extends AbstractEntity<Integer>> T getRepoObject(HttpServletRequest req, String idValue) {
        T result = null;
        if(req.getHttpServletMapping().getPattern().contains("cities")) {
            City city = new City();
            city.setId(Integer.parseInt(idValue));
            city.setCountry_id(0);
            city.setRegion_id(0);
            String[] param = req.getParameterValues("name");
            if (param.length == 0) {
                city.setName("NONAME");
            } else {
                city.setName(param[0]);
            }
            result = (T)city;
        }else if(req.getHttpServletMapping().getPattern().contains("regions")) {
            Region region = new Region();
            region.setId(Integer.parseInt(idValue));
            region.setCountry_id(0);
            region.setCity_id(0);
            String[] param = req.getParameterValues("name");
            if (param.length == 0) {
                region.setName("NONAME");
            } else {
                region.setName(param[0]);
            }
            result = (T)region;
        }else if(req.getHttpServletMapping().getPattern().contains("cities")) {
            Country country = new Country();
            country.setId(Integer.parseInt(idValue));
            country.setCity_id(0);
            String[] param = req.getParameterValues("name");
            if (param.length == 0) {
                country.setName("NONAME");
            } else {
                country.setName(param[0]);
            }
            result = (T)country;
        }
        return (T)result;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
        resp.getWriter().println("UPDATED!");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String idValue = getIdFromRequest(req);
        getCrudRepository(req.getHttpServletMapping().getPattern()).delete(Integer.parseInt(idValue));
        writer.println("Deleted!");
    }

    @Override
    public void destroy() {
        super.destroy();
    }


    @Override
    public void init() throws ServletException {
        try {
            Path fl1 = Files.createTempFile("city", "csv");
            cityRepository = new CityRepository(fl1.toString());

            Path fl2 = Files.createTempFile("region", "csv");
            regionRepository = new RegionRepository(fl2.toString());

            Path fl3 = Files.createTempFile("country", "csv");
            countryRepository = new CountryRepository(fl3.toString());

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
