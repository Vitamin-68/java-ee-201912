package ua.ithillel.dnepr.dml.rest.homework13;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableCaching
@EnableWebMvc
public class Homework13Application  {

    public static void main(String[] args) {
        SpringApplication.run(Homework13Application.class, args);
    }


}
