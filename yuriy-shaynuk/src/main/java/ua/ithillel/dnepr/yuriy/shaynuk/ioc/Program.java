package ua.ithillel.dnepr.yuriy.shaynuk.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class Program {
    private static H2Server h2Server;
    public static final String TYPE_CSV = "csv";
    public static final String TYPE_JDBC = "jdbc";

    public static String inputType;
    public static String inputPath;
    public static String outputType;
    public static String outputPath;
    public static String classString;

    public static void main(String[] args) {
        if(args.length != 5){
            log.error("application should have 5 params!");
            throw new IllegalArgumentException("application should have 5 params!");
        }else{
            inputType = getType(args[0]);
            inputPath = args[1];
            outputType = getType(args[2]);
            outputPath = args[3];
            classString = args[4];
        }
        log.debug(args.toString());

        AnnotationConfigApplicationContext context =  new AnnotationConfigApplicationContext(TransferData.class);
        context.getBean(TransferData.class).start();
        if(h2Server!=null){
            h2Server.stop();
        }
    }

    private static String getType(String str){
        String result;
        switch (str.toLowerCase()){
            case "csv": result = TYPE_CSV; break;
            case "jdbc": result = TYPE_JDBC; break;
            default: throw new NullPointerException("unknown repo type: "+str);
        }
        return result;
    }

    public static Class getClazz() {
        Class result;
        switch (classString.toLowerCase()){
            case "city":
                result = ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City.class;
                break;
            case "region":
                result = ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region.class;
                break;
            case "country":
                result = ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Country.class;
                break;
            default: throw new NullPointerException("unknown class: "+classString);
        }
        return result;
    }


    public static Connection startServer(String path){
        Connection connection =null;
        h2Server = new H2Server(NetUtils.getFreePort());
        try {
            h2Server.start();
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:".concat(path));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
