package ua.ithillel.dnepr.dml.ioc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;
import ua.ithillel.dnepr.dml.Repositories.JdbcCrudRepositoryImpl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class Programm {

    private CrudRepository inRepo, outRepo;
    public static Class objClass;
    public static String inputPath, outputPath;
    public static Integer objectId;

    private static AnnotationConfigApplicationContext context;

    public Programm(String[] args) {
        init(args);
    }

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(Application.class);
        Programm prg = new Programm(args);
        prg.transit(objectId);
    }

    private void transit(Integer id) {
        if (inRepo != null && outRepo != null) {
            Optional obj = inRepo.findById(id);
            if (obj.isPresent()) {
                outRepo.create((BaseEntity) obj.get());
            }
        }
    }

    public Integer init(String[] args) {
        Options options = new Options();
        options.addOption(prepeareOption("i", "input", "input source"));
        options.addOption(prepeareOption("o", "output", "output source"));
        options.addOption(prepeareOption("c", "class", "type name"));
        options.addOption(prepeareOption("id", "id", "object identificator"));
        CommandLineParser parser = new DefaultParser();
        HelpFormatter frmHelp = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (Exception e) {
            frmHelp.printHelp("for CSV file use full path to the file, for H2 DB use connection string (like: jdbc:h2:tcp://{host}:{port}/{dbName})", options);
            log.info("not found: ", e);
            System.exit(1);
        }
        inputPath = cmd.getOptionValue("input");
        outputPath = cmd.getOptionValue("output");
        String clazzName = cmd.getOptionValue("class");
        objectId = Integer.parseInt(cmd.getOptionValue("id"));
        try {
            objClass = Class.forName(clazzName);
        } catch (Exception e) {
            log.error("wrong class name:", e);
        }
        inRepo = configureRepo(inputPath, objClass);
        outRepo = configureRepo(outputPath, objClass);
        return objectId;
    }

    private CrudRepository configureRepo(String path, Class aClass) {
        CrudRepository result = null;
        if (path.toLowerCase().contains(".csv")) {
            result = (CrudRepository) context.getBean(aClass.getSimpleName().toLowerCase().concat("Repo"));
            if (result != null) {
                try {
                    Field pathField = result.getClass().getDeclaredField("filePath");
                    pathField.setAccessible(true);
                    pathField.set(result, path);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.error("wrong way ", e);
                }
            }
        } else if (path.toLowerCase().contains("jdbc:h2:")) {
            try {
                Class.forName("org.h2.Driver");
            } catch (Exception e) {
                log.error("Driver not found:", e);
            }
            try {
                result = (CrudRepository) context.getBean("h2Repo");
                Connection conn = DriverManager.getConnection(path, "sa", "");
                ((JdbcCrudRepositoryImpl) result).setConnection(conn);
            } catch (SQLException e) {
                log.error("connection problem ",e);
            }
        }
        return result;
    }

    private Option prepeareOption(String sName, String lName, String desc) {
        Option result = new Option(sName, lName, true, desc);
        result.setRequired(true);
        return result;
    }
}
