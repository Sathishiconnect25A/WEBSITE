package com.app.config;
 
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
@Configuration
public class FileResourceConfig implements WebMvcConfigurer {
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        registry.addResourceHandler("/alcohol/**")
                .addResourceLocations("file:C://alcohol/")
                .setCachePeriod(3600); 
        
//        registry.addResourceHandler(" D:/alcohol/**")
//        .addResourceLocations("file:D://alcohols/category/")
//        .setCachePeriod(3600);
    }
}