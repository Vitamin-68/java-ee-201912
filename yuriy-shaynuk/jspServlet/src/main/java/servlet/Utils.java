package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    public static synchronized File createTempFile(Class clazz, String resourcePath) {
        try {
            File tempFile = File.createTempFile("tmp", resourcePath);
            InputStream in = clazz.getClassLoader().getResourceAsStream(resourcePath);
            if (in != null) {
                try (FileOutputStream out = new FileOutputStream(tempFile)) {
                    //copy stream
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }
            return tempFile;
        } catch (IOException e) {
            return null;
        }
    }

    public static synchronized boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static synchronized String parseRequestBody(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }
}
