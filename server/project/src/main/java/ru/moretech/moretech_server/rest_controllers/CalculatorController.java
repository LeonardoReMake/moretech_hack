package ru.moretech.moretech_server.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.CalculateRequest;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.CalculateResponse;
import ru.moretech.moretech_server.Entities.calculatorEntities.settings.SettingsResponse;
import ru.moretech.moretech_server.work_with_vtb_api.CalculatorApi;

@RestController
@RequestMapping("/rest/calculator")
public class CalculatorController {

    @Autowired
    private CalculatorApi calculatorApi;

    @GetMapping("/settings")
    public SettingsResponse getSettings(@RequestParam(value = "name") String name,
                                        @RequestParam(value = "language") String language) throws JsonProcessingException {
        return calculatorApi.getSettings(name, language);
    }

    @PostMapping("/calculate")
    public CalculateResponse getCalculation(@RequestBody CalculateRequest request) throws JsonProcessingException {
        return calculatorApi.postCalculate(request);
    }
}
