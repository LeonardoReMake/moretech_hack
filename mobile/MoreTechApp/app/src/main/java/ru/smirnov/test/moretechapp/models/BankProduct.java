package ru.smirnov.test.moretechapp.models;

public class BankProduct {
    private final static String TAG = BankProduct.class.getName();

    public String title;
    public int imgRes;
    public String url;
    public int color;

    public BankProduct(String title, int imgRes, String url, int color) {
        this.title = title;
        this.imgRes = imgRes;
        this.url = url;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
