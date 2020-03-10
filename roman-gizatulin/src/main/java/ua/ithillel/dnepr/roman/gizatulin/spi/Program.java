package ua.ithillel.dnepr.roman.gizatulin.spi;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        List<Encoder> service = ServiceProvider.getServices(Encoder.class);

        String test = "hello world, привет мир";

        service.forEach(encoder -> {
            String data = new String(
                    encoder.encode(test.getBytes()),
                    StandardCharsets.UTF_8);
            System.out.println(String.format(
                    "Class: %s\nSource: %s\nDest: %s",
                    encoder.getClass().getName(),
                    test,
                    data));
        });

        ServiceProvider.reloadServices(Encoder.class);
    }
}
