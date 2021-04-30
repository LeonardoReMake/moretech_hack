package ru.moretech.moretech_server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.moretech.moretech_server"})
public class WebConfig implements WebMvcConfigurer {
    private static final Logger LOG = LoggerFactory.getLogger(WebConfig.class);
}
