package ru.moretech.moretech_server.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.moretech.moretech_server.Entities.clientEntities.Car;

import java.util.ArrayList;
import java.util.List;

public class CarDatasource extends Datasource {
    private static final Logger LOG = LoggerFactory.getLogger(CarDatasource.class);

    private final List<Car> cars = new ArrayList<>();

    public CarDatasource() {
        initCars();
    }

    public List<Car> getAllCars() {
        return cars;
    }

    public boolean addCar(Car car) {
        if (car.getCarBrand().equals("") || car.getMinprice() <= 0) {
            return false;
        }
        cars.add(car);
        return true;
    }

    private void initCars() {
        for (int i = 0; i < 5; i++) {
            String photo1 = "https://avatars.mds.yandex.net/get-autoru-vos/2049647/97faaddb8c6371be591ea9d76d4d4c2e/456x342n";
            String photo2 = "https://i.pinimg.com/originals/bf/6a/45/bf6a4535f0a4dc84adc69bd5b9902dd6.jpg";
            List<String> photos = new ArrayList<>();
            photos.add(photo1);
            photos.add(photo2);
            Car car = new Car("Mazda", 500000 + (i+1)*100,
                   "https://i.ytimg.com/vi/raV5eUYNlK8/maxresdefault.jpg",
                   "Rx8", "Rx8", photos);
            cars.add(car);
        }
    }
}
