package ru.moretech.moretech_server.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.moretech.moretech_server.Entities.RecognitionEntities.CarResponse;
import ru.moretech.moretech_server.Entities.RecognitionEntities.Content;
import ru.moretech.moretech_server.work_with_vtb_api.RecognitionApi;

@RestController
@RequestMapping("/rest/recognition")
public class RecognitionController {

    @Autowired
    private RecognitionApi recognitionApi;

    @PostMapping("/")
    public CarResponse getCatResponse(@RequestBody Content content) throws JsonProcessingException {
        CarResponse response = recognitionApi.getCarResponse(content);

        return null;
    }
}
