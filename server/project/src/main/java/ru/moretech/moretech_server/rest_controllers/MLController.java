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
import ru.moretech.moretech_server.datasource.CarDatasource;
import ru.moretech.moretech_server.model.RecognitionLogic;
import ru.moretech.moretech_server.work_with_vtb_api.MarketplaceApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/ml")
public class MLController {

    @Autowired
    private RecognitionLogic recognitionLogic;

    @PostMapping("/recognitionExtended")
    public CarResponse getCarResponse(@RequestBody Content content) {
        return recognitionLogic.recognizeExtended(content);
    }

    @PostMapping("/suggestion")
    public List<Car> getCarResponseWithConf(@RequestBody Content content) throws JsonProcessingException {
        return recognitionLogic.suggest(content);
    }
}
