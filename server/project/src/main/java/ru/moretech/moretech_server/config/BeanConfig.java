package ru.moretech.moretech_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.moretech.moretech_server.work_with_vtb_api.MarketplaceApi;
import ru.moretech.moretech_server.work_with_vtb_api.RecognitionApi;

@Configuration
public class BeanConfig {
    @Bean
    public MarketplaceApi getMarketplace() {
        return new MarketplaceApi();
    }

    @Bean
    public RecognitionApi getRecognition() {
        return new RecognitionApi();
    }
}
