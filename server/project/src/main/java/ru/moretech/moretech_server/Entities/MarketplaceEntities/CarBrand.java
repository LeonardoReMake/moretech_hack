package ru.moretech.moretech_server.Entities.MarketplaceEntities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarBrand {
    private String alias;
    private String logo;
    @JsonProperty
    private CarModel[] models;
    private String title;

    public CarModel[] getModels() {
        return models;
    }

    public void setModels(CarModel[] models) {
        this.models = models;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleRus() {
        return titleRus;
    }

    public void setTitleRus(String titleRus) {
        this.titleRus = titleRus;
    }

    private String titleRus;
}
