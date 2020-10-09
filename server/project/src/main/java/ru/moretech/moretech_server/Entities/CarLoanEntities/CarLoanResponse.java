package ru.moretech.moretech_server.Entities.CarLoanEntities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CarLoanResponse {
    @JsonProperty
    private CarLoanResponseApplication application;
    private String datetime;
    @JsonProperty
    private CarLoanResponseDecision decision_report;
}
