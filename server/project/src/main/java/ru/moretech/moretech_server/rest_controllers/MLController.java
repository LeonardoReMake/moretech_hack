package ru.moretech.moretech_server.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.moretech.moretech_server.Entities.RecognitionEntities.CarResponse;
import ru.moretech.moretech_server.Entities.RecognitionEntities.Content;
import ru.moretech.moretech_server.api.MLServerApi;

@RestController
@RequestMapping("/rest/ml")
public class MLController {
    @Value("${ibmClientId}")
    private String imbClientId;

    @Autowired
    private MLServerApi mlServerApi;

    @PostMapping("/recognition")
    public CarResponse getCarResponse(@RequestBody Content content) throws JsonProcessingException {
        return mlServerApi.getCarResponse(content);
    }

    @PostMapping("/recognition_conf")
    public CarResponse getCarResponseWithConf(@RequestBody Content content) {
        return null;
    }
}
