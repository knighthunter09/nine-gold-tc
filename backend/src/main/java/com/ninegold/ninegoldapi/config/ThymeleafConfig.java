package com.ninegold.ninegoldapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafConfig {

    @Bean
    public ITemplateResolver textTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/text/");
        resolver.setSuffix(".txt");
        resolver.setTemplateMode("TEXT");
        resolver.setCacheable(false);
        resolver.setCheckExistence(true);
        resolver.setOrder(1);
        return resolver;
    }

    @Bean
    public ITemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/html/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCheckExistence(true);
        resolver.setCacheable(false);
        return resolver;
    }
}