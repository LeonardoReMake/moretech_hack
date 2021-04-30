package ru.moretech.moretech_server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.BodyType;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.CarBrand;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.CarModel;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.Marketplace;
import ru.moretech.moretech_server.Entities.RecognitionEntities.CarResponse;
import ru.moretech.moretech_server.Entities.RecognitionEntities.Content;
import ru.moretech.moretech_server.Entities.clientEntities.Car;
import ru.moretech.moretech_server.api.MLServerApi;
import ru.moretech.moretech_server.datasource.CarDatasource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class RecognitionLogic {
    private static final Logger LOG = LoggerFactory.getLogger(RecognitionLogic.class);

    @Autowired
    private MLServerApi recognitionApi;

    @Autowired
    private CarDatasource carDatasource;

    public CarResponse recognizeExtended(Content content) {
        try {
            return recognitionApi.getCarResponse(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            CarResponse carResponse = new CarResponse();
            carResponse.setProbabilities(null);
            return carResponse;
        }
    }

    public List<Car> suggest(Content content) {
        String[] suggestedCars;
        try {
            suggestedCars = recognitionApi.getCarSuggestion(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        List<Car> listedCars = new ArrayList<>();
        List<Car> allCars = carDatasource.getAllCars();

        for (String suggestedCar : suggestedCars) {
            for (Car car : allCars) {
                String marketplaceBrandModel =
                        (car.getCarBrand() +
                                " " +
                                car.getTitle()).toLowerCase();

                if (marketplaceBrandModel.equals(suggestedCar.toLowerCase())) {
                    ArrayList<String> photos = new ArrayList<>();
                    photos.add(car.getPhoto());
                    photos.addAll(car.getPhotos());

                    listedCars.add(new Car(
                            car.getCarBrand(),
                            car.getMinprice(),
                            car.getPhoto(),
                            car.getTitle(),
                            car.getTitleRus(),
                            photos
                    ));
                }
            }
        }

        return listedCars;
    }
}
