package com.cafeteria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Exponer carpeta uploads/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + UPLOAD_DIR);
    }
}