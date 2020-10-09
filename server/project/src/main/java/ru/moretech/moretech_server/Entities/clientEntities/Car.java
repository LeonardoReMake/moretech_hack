package ru.moretech.moretech_server.Entities.clientEntities;

import java.util.List;

public class Car {
    private String carBrand;
    private int minprice;
    private String photo;
    private String title;
    private String titleRus;
    private List<String> photos;

    public Car(String carBrand, int minprice, String photo, String title, String titleRus, List<String> photos) {
        this.carBrand = carBrand;
        this.minprice = minprice;
        this.photo = photo;
        this.title = title;
        this.titleRus = titleRus;
        this.photos = photos;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public int getMinprice() {
        return minprice;
    }

    public void setMinprice(int minprice) {
        this.minprice = minprice;
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

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
