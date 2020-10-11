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
import ru.moretech.moretech_server.api.MLServerApi;
import ru.moretech.moretech_server.work_with_vtb_api.MarketplaceApi;
import ru.moretech.moretech_server.work_with_vtb_api.RecognitionApi;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/rest/recognition")
public class RecognitionController {
    private Logger logger = Logger.getLogger(RecognitionController.class.getName());
    @Autowired
    private RecognitionApi recognitionApi;

    @Autowired
    private MarketplaceApi marketplaceApi;

    @Autowired
    private MLServerApi mlServerApi;

    @PostMapping("/")
    public List<Car> getCatResponse(@RequestBody Content content) throws JsonProcessingException {
//        logger.log(Level.INFO, content.getContent());
        CarResponse responseFromVTBApi = recognitionApi.getCarResponse(content);
        CarResponse responseFromML = null;
        try {
            responseFromML = mlServerApi.getCarResponse(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Pair> listCars = new ArrayList<>();

        for (String s : responseFromVTBApi.getProbabilities().keySet()) {
            if (responseFromVTBApi.getProbabilities().get(s) > 0.3) {
                listCars.add(new Pair(s, responseFromVTBApi.getProbabilities().get(s)));
            }
        }

        if (responseFromML != null) {
            if (responseFromML.isConfidence()) {
                for (String s : responseFromML.getProbabilities().keySet()) {
                    if (responseFromML.getProbabilities().get(s) > 0.3) {
                        listCars.add(new Pair(s, responseFromML.getProbabilities().get(s)));
                    }
                }
            }
        }

        listCars.sort(Comparator.comparingDouble(value -> value.right));
        Collections.reverse(listCars);

        for (Pair listCar : listCars) {
            logger.log(Level.INFO, listCar.left + " " + listCar.right);
        }


        Marketplace marketplace = marketplaceApi.getMarketplace();
        List<Car> carList = new ArrayList<>();

        for (Pair listCar : listCars) {
            for (CarBrand carBrand : marketplace.getList()) {
                for (CarModel model : carBrand.getModels()) {
                    String marketplaceBrand = model.getBrand().getTitle();
                    String marketplaceModel = model.getTitle();
                    String marketplaceBrandModel = (marketplaceBrand + " " + marketplaceModel).toLowerCase();
                    String listModel = listCar.left.toLowerCase();

                    if (marketplaceBrandModel.equals(listModel)) {

                        ArrayList<String> photos = new ArrayList<>();
//                        Map<String, BodyType> render_main = model.getRenderPhotos().get("render_main");
//                        if (render_main != null) {
//                            for (BodyType bodyType : render_main.values()) {
//                                photos.add(bodyType.getPath());
//                            }
//                        }

                        Collection<Map<String, BodyType>> values = model.getRenderPhotos().values();

                        for (Map<String, BodyType> value : values) {
                            for (BodyType s : value.values()){
                                photos.add(s.getPath());
                            }
                        }


                        carList.add(new Car(model.getBrand().getTitle(),
                                model.getMinPrice(),
                                model.getPhoto(),
                                model.getTitle(),
                                model.getTitleRus(),
                                photos));
                    }
                }
            }
        }

        logger.log(Level.INFO, carList.toString());
        return carList;
    }


    @PostMapping("/suggestion")
    public List<Car> getSuggestion(@RequestBody Content content) throws JsonProcessingException {
        String[] suggestions = mlServerApi.getCarSuggestion(content);
        Marketplace marketplace = marketplaceApi.getMarketplace();
        List<Car> listedCars = new ArrayList<>();

        for (CarBrand carBrand : marketplace.getList()) {
            for (CarModel model : carBrand.getModels()) {
                String marketplaceBrand = model.getBrand().getTitle();
                String marketplaceModel = model.getTitle();
                String marketplaceBrandModel = (marketplaceBrand + " " + marketplaceModel).toLowerCase();

                for (String suggestion : suggestions) {
                    if (marketplaceBrandModel.equals(suggestion)) {

                        ArrayList<String> photos = new ArrayList<>();
                        Collection<Map<String, BodyType>> values = model.getRenderPhotos().values();

                        for (Map<String, BodyType> value : values) {
                            for (BodyType s : value.values()) {
                                photos.add(s.getPath());
                            }
                        }


                        listedCars.add(new Car(model.getBrand().getTitle(),
                                model.getMinPrice(),
                                model.getPhoto(),
                                model.getTitle(),
                                model.getTitleRus(),
                                photos));
                    }
                }
            }
        }
        return listedCars;
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
