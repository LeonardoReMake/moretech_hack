package ru.smirnov.test.moretechapp.models;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.lifecycle.ViewModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Car extends ViewModel {
    private String id;
    private String brand;
    private int minPrice;
    private String photo;
    private String model;
    private String titleRus;
    private String[] photoLinks;
    private CarPhoto[] photos;

    private String body;
    private int doors;
    private int colors;

    private ArrayList<Bitmap> images;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTitleRus() {
        return titleRus;
    }

    public void setTitleRus(String titleRus) {
        this.titleRus = titleRus;
    }

    public String[] getPhotoLinks() {
        return photoLinks;
    }

    public void setPhotoLinks(String[] photoLinks) {
        this.photoLinks = new String[photoLinks.length+1];
        if (this.photo != null) {
            this.photoLinks[0] = this.photo;
        }
        int i = 1;
        for (String photo : photoLinks) {
            this.photoLinks[i] = photo;
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

    public CarPhoto[] getPhotos() {
        return photos;
    }

    public void setPhotos(CarPhoto[] photos) {
        this.photos = photos;
        this.photoLinks = (String[]) Arrays.stream(photos).map(CarPhoto::getLink).toArray();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
