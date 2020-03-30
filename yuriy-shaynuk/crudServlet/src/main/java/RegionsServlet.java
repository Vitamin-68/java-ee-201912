import com.google.gson.Gson;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/regions/*")
public class RegionsServlet extends HttpServlet {
    static private CrudRepository<Region, Integer> crudRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        executeMethod(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        executeMethod(request,response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        executeMethod(request,response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        executeMethod(request,response);
    }

    private void executeMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String id = request.getRequestURI().replace("/regions/", "");
        if (Utils.isInteger(id)) {
            int entityId = Integer.parseInt(id);
            String requestMethod = request.getMethod();
            if(requestMethod.equals(HttpMethod.GET) || requestMethod.equals(HttpMethod.DELETE)){
                Optional<Region> region = Optional.empty();
                switch (requestMethod) {
                    case HttpMethod.GET: {
                        region = crudRepository.findById(entityId);
                    }
                    break;
                    case HttpMethod.DELETE: {
                        region = Optional.ofNullable(crudRepository.delete(entityId));
                    }
                    break;
                }
                if (region.isPresent()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    String json = gson.toJson(region.get());
                    out.println(json);
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            }else if(requestMethod.equals(HttpMethod.POST) || requestMethod.equals(HttpMethod.PUT)){
                String postBody = Utils.parseRequestBody(request.getInputStream());
                Region newRegion = gson.fromJson(postBody, Region.class);
                newRegion.setId(entityId);
                if(crudRepository.findById(entityId).isPresent()){
                    if(requestMethod.equals(HttpMethod.POST)) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                    }else{
                        crudRepository.update(newRegion);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }else{
                    if(requestMethod.equals(HttpMethod.POST)) {
                        crudRepository.create(newRegion);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }else{
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    }
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void init() {
        File dataFile = Utils.createTempFile(getClass(),"region.csv");
        if (dataFile != null) {
            crudRepository = new CrudRepositoryImp<>(dataFile.getPath(),Region.class);
        }
    }
}
