import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static File createTempFile(Class clazz, String resourcePath) {
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
            //log.debug("tmp file path: "+tempFile.getAbsolutePath());
            return tempFile;
        } catch (IOException e) {
            //log.error("getResourceAsFile exception",e);
            return null;
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
