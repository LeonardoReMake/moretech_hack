package ru.moretech.moretech_server.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.moretech.moretech_server.Entities.Marketplace;
import ru.moretech.moretech_server.work_with_vtb_api.MarketplaceApi;

@RestController
@RequestMapping("/rest/marketplace")
public class MarketplaceController {

    @Autowired
    private MarketplaceApi marketplaceApi;

    @GetMapping("/")
    public Marketplace getMarketplace() {
        try {
            return marketplaceApi.getMarketplace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
