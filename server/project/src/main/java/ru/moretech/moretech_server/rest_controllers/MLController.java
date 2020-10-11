package ru.moretech.moretech_server.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.BodyType;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.CarBrand;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.CarModel;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.Marketplace;
import ru.moretech.moretech_server.Entities.RecognitionEntities.CarResponse;
import ru.moretech.moretech_server.Entities.RecognitionEntities.Content;
import ru.moretech.moretech_server.Entities.clientEntities.Car;
import ru.moretech.moretech_server.api.MLServerApi;
import ru.moretech.moretech_server.work_with_vtb_api.MarketplaceApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/ml")
public class MLController {
    @Value("${ibmClientId}")
    private String imbClientId;

    @Autowired
    private MLServerApi mlServerApi;

    @Autowired
    private MarketplaceApi marketplaceApi;

    @PostMapping("/recognitionExtended")
    public CarResponse getCarResponse(@RequestBody Content content) throws JsonProcessingException {
        return mlServerApi.getCarResponse(content);
    }

    @PostMapping("/suggestion")
    public List<Car> getCarResponseWithConf(@RequestBody Content content) throws JsonProcessingException {
        String[] suggestedCars = mlServerApi.getCarSuggestion(content);
        List<Car> listedCars = new ArrayList<>();
        Marketplace marketplace = marketplaceApi.getMarketplace();

        for (String suggestedCar : suggestedCars) {
            for (CarBrand carBrand : marketplace.getList()) {
                for (CarModel model : carBrand.getModels()) {
                    String marketplaceBrandModel =
                            (model.getBrand().getTitle() +
                                    " " +
                                    model.getTitle()).toLowerCase();

                    if (marketplaceBrandModel.equals(suggestedCar.toLowerCase())) {
                        ArrayList<String> photos = new ArrayList<>();
                        Map<String, BodyType> render_main = model.getRenderPhotos().get("render_main");

                        if (render_main != null) {
                            for (BodyType bodyType : render_main.values()) {
                                photos.add(bodyType.getPath());
                            }
                        }

                        listedCars.add(new Car(
                                model.getBrand().getTitle(),
                                model.getMinPrice(),
                                model.getPhoto(),
                                model.getTitle(),
                                model.getTitleRus(),
                                photos
                        ));
                    }
                }
            }
        }

        return listedCars;
    }
}
