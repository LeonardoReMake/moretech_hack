package ru.moretech.moretech_server.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.moretech.moretech_server.Entities.RecognitionEntities.CarResponse;
import ru.moretech.moretech_server.work_with_vtb_api.RecognitionApi;

@RestController
@RequestMapping("/rest/recognition")
public class RecognitionController {

    @Autowired
    private RecognitionApi recognitionApi;

    @PostMapping("/")
    public CarResponse getCatResponse(@RequestParam String image) {

        return null;
    }
}
