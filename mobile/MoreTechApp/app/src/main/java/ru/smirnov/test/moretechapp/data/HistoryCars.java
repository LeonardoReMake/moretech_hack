package ru.smirnov.test.moretechapp.data;

import java.util.ArrayList;
import java.util.List;

import ru.smirnov.test.moretechapp.models.Car;

public final class HistoryCars {
    private final static String TAG = HistoryCars.class.getName();

    private List<Car> cars = new ArrayList<>();

    private static HistoryCars instance;

    private HistoryCars() {

    }

    public static HistoryCars getInstance() {
        if (instance == null) {
            instance = new HistoryCars();
        }
        return instance;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public void addCar(Car car) {
        for (Car carAr : cars) {
            if (carAr.getId() == car.getId()) {
                return;
            }
        }
        cars.add(car);
    }
}
