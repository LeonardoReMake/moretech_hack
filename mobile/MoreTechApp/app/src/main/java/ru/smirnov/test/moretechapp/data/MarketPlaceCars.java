package ru.smirnov.test.moretechapp.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.smirnov.test.moretechapp.models.Car;

final public class MarketPlaceCars {
    private final static String TAG = MarketPlaceCars.class.getName();

    private static MarketPlaceCars instance;

    private List<Car> cars;

    public static String marketplaceUrl = "http://10.55.128.106:8080/rest/marketplace/";

    private MarketPlaceCars() {
    }

    public static MarketPlaceCars getInstance() {
        if (instance == null) {
            instance = new MarketPlaceCars();
        }
        return instance;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<Car> getCars(int count) {
        if (cars == null) {
            cars = new ArrayList<>();
        }

        if (count < cars.size()) {
            return cars.subList(0, count);
        }

        return cars;
    }

    public void setCars(Car[] cars) {
        if (this.cars == null) {
            this.cars = new ArrayList<>();
        }
        Collections.addAll(this.cars, cars);
    }
}
