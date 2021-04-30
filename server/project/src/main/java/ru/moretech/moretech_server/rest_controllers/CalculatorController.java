package ru.moretech.moretech_server.rest_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.CalculateRequest;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.CalculateResponse;
import ru.moretech.moretech_server.Entities.calculatorEntities.settings.SettingsResponse;
import ru.moretech.moretech_server.model.Calculator;

@RestController
@RequestMapping("/rest/calculator")
public class CalculatorController {

    @Autowired
    private Calculator calculator;

    @GetMapping("/settings")
    public SettingsResponse getSettings(@RequestParam(value = "name") String name,
                                        @RequestParam(value = "language") String language) {
        return calculator.receiveSettings(name, language);
    }

    @PostMapping("/calculate")
    public CalculateResponse getCalculation(@RequestBody CalculateRequest request) {
        return calculator.calculate(request);
    }
}
