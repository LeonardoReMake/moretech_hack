package ru.moretech.moretech_server.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.BodyType;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.CarBrand;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.CarModel;
import ru.moretech.moretech_server.Entities.MarketplaceEntities.Marketplace;
import ru.moretech.moretech_server.Entities.clientEntities.Car;
import ru.moretech.moretech_server.work_with_vtb_api.MarketplaceApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/marketplace")
public class MarketplaceController {

    @Autowired
    private MarketplaceApi marketplaceApi;

    @GetMapping("/")
    public List<Car> getCars() {
        List<Car> carList = new ArrayList<>();
        try {
            Marketplace marketplace = marketplaceApi.getMarketplace();

            for (CarBrand carBrand : marketplace.getList()) {
                for (CarModel model : carBrand.getModels()) {
                    ArrayList<String> photos = new ArrayList<>();
//                    Map<String, BodyType> render_main = model.getRenderPhotos().get("render_main");
//
//                    if (render_main != null) {
//                        for (BodyType bodyType : render_main.values()) {
//                            photos.add(bodyType.getPath());
//                        }
//                    }

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

            return carList;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
