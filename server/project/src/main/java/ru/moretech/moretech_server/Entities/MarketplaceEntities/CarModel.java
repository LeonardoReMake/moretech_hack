package ru.moretech.moretech_server.Entities.MarketplaceEntities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarModel {
    private String alias;
    @JsonProperty
    private CarBody[] bodies;
    private CarBrandOnly brand;
    private int minPrice;
    private CarModelModel model;
    private String photo;
    private String title;
    private String titleRus;
    private TransportType transportType;
    @JsonProperty
    private Map<String, Map<String, BodyType>> renderPhotos;

    public CarBody[] getBodies() {
        return bodies;
    }

    public Map<String, Map<String, BodyType>> getRenderPhotos() {
        return renderPhotos;
    }

    public void setRenderPhotos(Map<String, Map<String, BodyType>> renderPhotos) {
        this.renderPhotos = renderPhotos;
    }

    public void setBodies(CarBody[] bodies) {
        this.bodies = bodies;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public CarBrandOnly getBrand() {
        return brand;
    }

    public void setBrand(CarBrandOnly brand) {
        this.brand = brand;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public CarModelModel getModel() {
        return model;
    }

    public void setModel(CarModelModel model) {
        this.model = model;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }
}
