package ru.moretech.moretech_server.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.AddCarResponse;
import ru.moretech.moretech_server.Entities.clientEntities.Car;
import ru.moretech.moretech_server.model.CarLogic;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/marketplace")
public class MarketplaceController {

    @Autowired
    private CarLogic carLogic;

    @GetMapping("/")
    public List<Car> getCars() {
        return carLogic.catalog();
    }

    @PostMapping("/add")
    public AddCarResponse addCar(@RequestBody Car car) {
        return carLogic.addCar(car);
    }

    @GetMapping("/add")
    public AddCarResponse addCar(@RequestParam String carBrand,
                                 @RequestParam int price,
                                 @RequestParam(required = false) String photo,
                                 @RequestParam(required = false) String model) {
        Car car = new Car(carBrand, price, photo, model, model, new ArrayList<>());
        return carLogic.addCar(car);
    }
}
