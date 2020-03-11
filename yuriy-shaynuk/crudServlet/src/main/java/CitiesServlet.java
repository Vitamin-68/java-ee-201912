import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/cities/*")
public class CitiesServlet extends HttpServlet {
//    static final H2Server h2Server = new H2Server(12345);
//    static private EntityManagerFactory entityManagerFactory;
//    static private SpringCityRepositoryImp cityRepository;
//    static private SpringRegionRepositoryImp regionRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h3>Helloooo World!</h3>");
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
        super.init();

//        final AnnotationConfigApplicationContext annotationConfigApplicationContext =
//                new AnnotationConfigApplicationContext(AppConfig.class);
//        entityManagerFactory = annotationConfigApplicationContext.getBean(EntityManagerFactory.class);
//        cityRepository = annotationConfigApplicationContext.getBean(SpringCityRepositoryImp.class);
//        regionRepository = annotationConfigApplicationContext.getBean(SpringRegionRepositoryImp.class);
    }
}
