package ru.moretech.moretech_server.model;

import ru.moretech.moretech_server.datasource.CalculatorDatasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.CalculateRequest;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.CalculateResponse;
import ru.moretech.moretech_server.Entities.calculatorEntities.settings.SettingsResponse;

@Component
public class Calculator {
    private static final Logger LOG = LoggerFactory.getLogger(Calculator.class);

    private final CalculatorDatasource calculatorDatasource;

    @Autowired
    public Calculator(CalculatorDatasource calculatorDatasource) {
        this.calculatorDatasource = calculatorDatasource;
    }

    public SettingsResponse receiveSettings(String name, String lang) {
        calculatorDatasource.saveSettings(name, lang);
        return new SettingsResponse();
    }

    public CalculateResponse calculate(CalculateRequest calculateRequest) {
        CalculateResponse calculateResponse = new CalculateResponse();
        return calculateResponse;
    }

}
