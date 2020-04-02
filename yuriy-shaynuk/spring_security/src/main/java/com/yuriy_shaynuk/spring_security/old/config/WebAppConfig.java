package com.yuriy_shaynuk.spring_security.old.config;

import com.yuriy_shaynuk.spring_security.old.service.UserDetailsServiceImpl;
import com.yuriy_shaynuk.spring_security.old.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("com.yuriy_shaynuk.spring_security")
public class WebAppConfig implements WebMvcConfigurer {
    @Autowired
    private UserService userService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/pages/**")
                .addResourceLocations("/pages/");
    }

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl(userService);
    }

    @Bean
    public UrlBasedViewResolver setupViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/pages/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);

        return resolver;
    }

}

