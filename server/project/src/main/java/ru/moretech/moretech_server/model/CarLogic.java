package ru.moretech.moretech_server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.AddCarResponse;
import ru.moretech.moretech_server.Entities.clientEntities.Car;
import ru.moretech.moretech_server.datasource.CarDatasource;

import java.util.List;

@Component
public class CarLogic {
    private static final Logger LOG = LoggerFactory.getLogger(CarLogic.class);

    private final CarDatasource carDatasource;

    @Autowired
    public CarLogic(CarDatasource carDatasource) {
        this.carDatasource = carDatasource;
    }

    public List<Car> catalog() {
        return carDatasource.getAllCars();
    }

    public AddCarResponse addCar(Car car) {
        boolean isAdded = carDatasource.addCar(car);
        if (isAdded) {
            return AddCarResponse.successful();
        } else {
            return AddCarResponse.error("Car brand must be specified or price could not be below zero.");
        }
    }
}
