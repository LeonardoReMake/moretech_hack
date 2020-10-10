package ru.moretech.moretech_server.Entities.RecognitionEntities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;


public class CarResponse {
    @JsonProperty
    private Map<String, Double> probabilities;
    private boolean confidence = true;

    public boolean isConfidence() {
        return confidence;
    }

    public void setConfidence(boolean confidence) {
        this.confidence = confidence;
    }

    public Map<String, Double> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(Map<String, Double> probabilities) {
        this.probabilities = probabilities;
    }
}
