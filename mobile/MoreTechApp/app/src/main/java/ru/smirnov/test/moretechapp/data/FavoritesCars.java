package ru.smirnov.test.moretechapp.data;

import java.util.ArrayList;
import java.util.List;

import ru.smirnov.test.moretechapp.models.Car;

public final class FavoritesCars {
    private final static String TAG = FavoritesCars.class.getName();

    private static FavoritesCars instance;

    private List<Car> favCars = new ArrayList<>();

    private FavoritesCars() {

    }

    public static FavoritesCars getInstance() {
        if (instance == null) {
            instance = new FavoritesCars();
        }
        return instance;
    }

    public void addFav(Car car) {
        favCars.add(car);
    }

    public List<Car> getFavCars() {
        return favCars;
    }

    public boolean isFav(int id) {
        for (Car car : favCars) {
            if (car.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void remove(int id) {
        int removeIndex = 0;
        for (int i = 0; i < favCars.size(); i++) {
            if (favCars.get(i).getId() == id) {
                removeIndex = id;
            }
        }
        favCars.remove(removeIndex);
    }
}
