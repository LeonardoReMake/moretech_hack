package ru.smirnov.test.moretechapp.models;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Car {
    private String carBrand;
    private int minprice;
    private String photo;
    private String title;
    private String titleRus;
    private String[] photos;

    private ArrayList<Bitmap> images;

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

    public String[] getPhotos() {
        return photos;
    }

    public void setPhotos(String[] photos) {
        this.photos = photos;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images = images;
    }

    public void addImage(Bitmap bitmap) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(bitmap);
    }
}
