package ua.ithillel.dnepr.roman.gizatulin.jservlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Program extends HttpServlet {
        private Path dirPath;

    @Override
    public void init() throws ServletException {
        try {
            dirPath = Files.createTempDirectory("r.g.servlet");
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result = "";
        //TODO: string lines to one string
        //Stream.of(Files.readAllLines(dirPath)).map()
        resp.getWriter().write(result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO: Get file path from request
        // if (file exists)
        // resp.setStatus(HttpServletResponse.SC_FOUND);
        // else
        //TODO: Create new file + write data
        //resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
