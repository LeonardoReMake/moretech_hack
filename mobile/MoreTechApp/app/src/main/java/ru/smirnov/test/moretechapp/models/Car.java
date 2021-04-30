package ru.smirnov.test.moretechapp.models;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

import androidx.lifecycle.ViewModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Car extends ViewModel {
    private int id;
    private String carBrand;
    private int minprice;
    private String photo;
    private String title;
    private String titleRus;
    private String[] photos;

    private String body;
    private int doors;
    private int colors;

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
        this.photos = new String[photos.length+1];
        if (this.photo != null) {
            this.photos[0] = this.photo;
        }
        int i = 1;
        for (String photo : photos) {
            this.photos[i] = photo;
            i++;
        }
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }

    public int getColors() {
        return colors;
    }

    public void setColors(int colors) {
        this.colors = colors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
