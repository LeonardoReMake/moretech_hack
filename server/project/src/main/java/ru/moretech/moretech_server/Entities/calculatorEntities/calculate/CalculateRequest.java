package ru.moretech.moretech_server.Entities.calculatorEntities.calculate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculateRequest {
    @JsonProperty
    private String[] clientTypes;
    private double cost;
    private double initialFee;
    private int kaskoValue;
    private String language;
    private double residualPayment;
    private String settingsName;
    @JsonProperty
    private String[] specialConditions;
    private int term;

    public String[] getClientTypes() {
        return clientTypes;
    }

    public void setClientTypes(String[] clientTypes) {
        this.clientTypes = clientTypes;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getInitialFee() {
        return initialFee;
    }

    public void setInitialFee(double initialFee) {
        this.initialFee = initialFee;
    }

    public int getKaskoValue() {
        return kaskoValue;
    }

    public void setKaskoValue(int kaskoValue) {
        this.kaskoValue = kaskoValue;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getResidualPayment() {
        return residualPayment;
    }

    public void setResidualPayment(double residualPayment) {
        this.residualPayment = residualPayment;
    }

    public String getSettingsName() {
        return settingsName;
    }

    public void setSettingsName(String settingsName) {
        this.settingsName = settingsName;
    }

    public String[] getSpecialConditions() {
        return specialConditions;
    }

    public void setSpecialConditions(String[] specialConditions) {
        this.specialConditions = specialConditions;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }
}
