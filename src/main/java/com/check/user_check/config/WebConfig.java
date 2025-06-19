package com.check.user_check.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://192.168.0.8:8081", "https://worldpark.github.io")
                .allowedMethods("GET", "POST")
                .allowCredentials(true)
                .maxAge(3000);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

//        registry.addViewController("/{spring:\\w+}")
//                .setViewName("forward:/index.html");

        registry.addViewController("/admin/**")
                .setViewName("forward:/index.html");

        registry.addViewController("/user/**")
                .setViewName("forward:/index.html");
    }
}
