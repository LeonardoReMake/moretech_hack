package ru.moretech.moretech_server.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.BodyType;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.CarBrand;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.CarModel;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.Marketplace;
import ru.moretech.moretech_server.Entities.RecognitionEntities.CarResponse;
import ru.moretech.moretech_server.Entities.RecognitionEntities.Content;
import ru.moretech.moretech_server.Entities.clientEntities.Car;
import ru.moretech.moretech_server.work_with_vtb_api.MarketplaceApi;
import ru.moretech.moretech_server.work_with_vtb_api.RecognitionApi;

import java.util.*;


@RestController
@RequestMapping("/rest/recognition")
public class RecognitionController {

    @Autowired
    private RecognitionApi recognitionApi;

    @Autowired
    private MarketplaceApi marketplaceApi;

    @PostMapping("/")
    public List<Car> getCatResponse(@RequestBody Content content) throws JsonProcessingException {
        CarResponse responseFromVTBApi = recognitionApi.getCarResponse(content);
        List<Pair> listCars = new ArrayList<>();

        for (String s : responseFromVTBApi.getProbabilities().keySet()) {
            listCars.add(new Pair(s, responseFromVTBApi.getProbabilities().get(s)));
        }

        listCars.sort(Comparator.comparingDouble(value -> value.right));
        Collections.reverse(listCars);

        Marketplace marketplace = marketplaceApi.getMarketplace();
        List<Car> carList = new ArrayList<>();

        for (Pair listCar : listCars) {
            for (CarBrand carBrand : marketplace.getList()) {
                for (CarModel model : carBrand.getModels()) {
                    String marketplaceBrandModel = model.getBrand().getTitle() + " " + model.getTitle();
                    if (marketplaceBrandModel.equals(listCar.left)) {

                        ArrayList<String> photos = new ArrayList<>();
                        Map<String, BodyType> render_main = model.getRenderPhotos().get("render_main");

                        if (render_main != null) {
                            for (BodyType bodyType : render_main.values()) {
                                photos.add(bodyType.getPath());
                            }
                        }

                        carList.add(new Car(model.getBrand().getTitle(),
                                model.getMinprice(),
                                model.getPhoto(),
                                model.getTitle(),
                                model.getTitleRus(),
                                photos));
                    }
                }
            }
        }
        return carList;
    }

    static class Pair {
        public String left;
        public Double right;

        public Pair(String left,
                    Double right) {
            this.left = left;
            this.right = right;
        }
    }
}
