package ru.smirnov.test.moretechapp.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.smirnov.test.moretechapp.BuildConfig;
import ru.smirnov.test.moretechapp.models.Car;

final public class MarketPlaceCars {
    private final static String TAG = MarketPlaceCars.class.getName();

    private static MarketPlaceCars instance;

    private List<Car> cars;

    public static String marketplaceUrl = BuildConfig.hostUrl;

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

    public Car getForId(String id) {
        for (Car car : cars) {
            if (car.getId().equals(id)) {
                return car;
            }
        }
        return null;
    }

    public Car searchForBrandName(String brand, String model) {
        for (Car car : cars) {
            if (car.getBrand().equalsIgnoreCase(brand) &&
                    car.getModel().equalsIgnoreCase(model)) {
                return car;
            }
        }
        return null;
    }

    public Car searchForBrandName(Car carInto) {
        for (Car car : cars) {
            if (car.getBrand().equalsIgnoreCase(carInto.getBrand()) &&
                    car.getModel().equalsIgnoreCase(carInto.getModel())) {
                return car;
            }
        }
        return null;
    }
}
