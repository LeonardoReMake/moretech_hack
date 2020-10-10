package ru.moretech.moretech_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.moretech.moretech_server.api.MLServerApi;
import ru.moretech.moretech_server.work_with_vtb_api.CalculatorApi;
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

    @Bean
    public CalculatorApi getCalculator() {
        return new CalculatorApi();
    }

    @Bean
    public MLServerApi getMLApi() {
        return new MLServerApi();
    }
}
