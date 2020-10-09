package ru.moretech.moretech_server.Entities.RecognitionEntities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;


public class CarResponse {
    @JsonProperty
    private Map<String, Double> probabilities;
}
