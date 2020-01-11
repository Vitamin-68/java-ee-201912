package ua.ithillel.alex.tsiba.repository.stores;

import lombok.Getter;
import ua.ithillel.alex.tsiba.repository.annotations.Column;
import ua.ithillel.alex.tsiba.repository.annotations.Table;
import ua.ithillel.alex.tsiba.repository.exception.DataStoreException;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVDataStore<EntityType extends BaseEntity> implements DataStore<EntityType> {
    @Getter
    private Class objClass;
    private String encoding = "UTF-8";
    private String separator = ";";
    private File storeFile;
    private List<String> columns = new ArrayList<>();
    private BufferedReader bufferedReader;

    public CSVDataStore(Class objClass) throws IOException, DataStoreException {
        this.objClass = objClass;

        final Table tableAnnotation = (Table) objClass.getDeclaredAnnotation(Table.class);
        final String table = tableAnnotation.table();

        if (table == null || "".equals(table)) {
            throw new IllegalStateException("Object don't has table annotation;");
        }

        storeFile = new File(this.getClass().getResource(
                ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + table + ".csv"
        ).getFile());
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(storeFile), encoding));
        loadFields();
    }

    @Override
    public void save(Collection<EntityType> objList) throws DataStoreException {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(storeFile), encoding));
            saveFields(bufferedWriter);
            for (EntityType object : objList) {
                saveObject(bufferedWriter, object);
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new DataStoreException("Data cannot be saved.", e);
        }
    }

    @Override
    public EntityType load() throws DataStoreException {
        EntityType result = null;
        try {
            String fieldsString = bufferedReader.readLine();
            if (fieldsString != null) {
                String[] fieldValues = fieldsString.split(separator);
                result = (EntityType) objClass.getConstructor().newInstance();

                for (Field field : getAllFields(new ArrayList<Field>(), objClass)) {
                    Column fieldAnnotation = field.getAnnotation(Column.class);
                    if (fieldAnnotation != null) {
                        String columnName = fieldAnnotation.name();
                        boolean isId = fieldAnnotation.isId();
                        Column.ColumnProperty type = fieldAnnotation.property();

                        if (columnName != null && !"".equals(columnName)) {
                            Integer key = columns.indexOf(columnName);
                            if (fieldValues.length >= key) {
                                String fieldValue = fieldValues[key];
                                field.setAccessible(true);
                                switch (type) {
                                    case INTEGER:
                                    case AUTO_INCREMENT:
                                        field.set(result, Integer.parseInt(fieldValue));
                                        break;
                                    default:
                                        field.set(result, fieldValue);
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DataStoreException("Data cannot be uploaded.", e);
        }
        return result;
    }

    @Override
    public boolean fieldExist(String name) {
        return columns.contains(name);
    }

    private List<Field> getAllFields(List fields, Class objClass) {
        for (Field field : objClass.getDeclaredFields()) {
            if (!fields.contains(field)) {
                fields.add(field);
            }
        }

        if (objClass.getSuperclass() != null) {
            getAllFields(fields, objClass.getSuperclass());
        }

        return fields;
    }

    private void loadFields() throws IOException, DataStoreException {
        String columnsString = bufferedReader.readLine();

        if (columnsString == null || "".equals(columnsString)) {
            throw new DataStoreException("Columns name not found!");
        }

        for (String column : columnsString.split(separator)) {
            if (columns.contains(column)) {
                throw new DataStoreException("Duplicate columns");
            }
            columns.add(column);
        }
    }

    private void saveFields(BufferedWriter writer) throws DataStoreException {
        if (columns == null) {
            throw new DataStoreException("Column for save not found");
        }
        String columnsString = "";
        String sep = "";

        for (String column : columns) {
            columnsString += sep + column;
            sep = separator;
        }

        try {
            writer.write(columnsString);
            writer.newLine();
        } catch (IOException e) {
            throw new DataStoreException("", e);
        }
    }

    private void saveObject(BufferedWriter writer, EntityType object) throws DataStoreException {
        Map<String, String> fieldValues = new HashMap<>();

        try {
            String saveString = "";
            String sep = "";

            for (Field field : getAllFields(new ArrayList(), objClass)) {
                Column fieldAnnotation = field.getAnnotation(Column.class);
                if (fieldAnnotation != null) {
                    String columnName = fieldAnnotation.name();

                    if (columnName != null && !"".equals(columnName)) {
                        field.setAccessible(true);
                        fieldValues.put(columnName, String.valueOf(field.get(object)));
                    }
                }
            }

            for (String column : columns) {
                String value = "";
                if (fieldValues.containsKey(column)) {
                    value = fieldValues.get(column);
                }

                saveString += sep + value;
                sep = separator;
            }

            if (!"".equals(saveString)) {
                writer.write(saveString);
                writer.newLine();
            }
        } catch (IOException | IllegalAccessException e) {
            throw new DataStoreException("", e);
        }
    }
}
