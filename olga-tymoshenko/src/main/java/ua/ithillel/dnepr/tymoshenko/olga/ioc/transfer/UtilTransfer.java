package ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.tymoshenko.olga.util.EntityWorker;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UtilTransfer<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> {

    public Class<? extends AbstractEntity> getClass(String pathFile) {
        Class<? extends AbstractEntity> clazz;
        File file = new File(pathFile);
        String fileName = StringUtils.removeEnd(file.getName(), ".csv");
        StringBuilder nameClass = new StringBuilder();
        nameClass.append("ua.ithillel.dnepr.tymoshenko.olga.entity.")
                .append(fileName.substring(0, 1).toUpperCase())
                .append(fileName.substring(1).toLowerCase());
        try {
            clazz = (Class<? extends AbstractEntity>) Class.forName(nameClass.toString());
        } catch (ClassNotFoundException e) {
           log.error(e.getMessage());
            throw new IllegalStateException(e);
        }
        return clazz;
    }

    public Optional<List<Field>> getListField(Class<? extends AbstractEntity> clazz) {
        List<Field> listField;
        try {
            EntityWorker entityWorker = new EntityWorker(clazz.getConstructor().newInstance());
            listField = entityWorker.getFields();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
             log.error("Failed to create entity instance");
            throw new IllegalStateException("Failed to create entity instance", e);
        }
        return listField.isEmpty() ? Optional.empty() : Optional.of(listField);
    }

    public String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else return "";
    }

    public Connection getBaseConnection(String pathDB) {
        Connection connection;
        StringBuilder urlDB = new StringBuilder();
        urlDB.append("jdbc:h2:").append(pathDB);

        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(urlDB.toString());
        } catch (ClassNotFoundException | SQLException e) {
           log.error(e.getMessage());
            throw new IllegalStateException(e);
        }
        return connection;
    }

    public void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            log.error(e.getSQLState());
            throw new IllegalStateException(e);
        }
    }
}
