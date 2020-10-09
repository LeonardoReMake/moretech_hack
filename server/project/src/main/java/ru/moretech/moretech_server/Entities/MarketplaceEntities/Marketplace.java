package ru.moretech.moretech_server.Entities.MarketplaceEntities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Marketplace {

    @JsonProperty
    private CarBrand[] list;

    public CarBrand[] getList() {
        return list;
    }

    public void setList(CarBrand[] list) {
        this.list = list;
    }
}
