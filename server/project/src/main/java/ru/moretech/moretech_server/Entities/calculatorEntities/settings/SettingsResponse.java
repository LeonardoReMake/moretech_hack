package ru.moretech.moretech_server.Entities.calculatorEntities.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SettingsResponse {
    private String anchor;
    @JsonProperty
    private String[] clientTypes = {"fds", "dsfds", "dsf"};
    private double cost;
    private double initialFee;
    private int kaskoDefaultValue;
    private String language;
    private String name;
    private boolean openInNewTab;
    @JsonProperty
    private String[] programs = {"loan"};
    @JsonProperty
    private SpecialConditions[] specialConditions = {new SpecialConditions()};
    @JsonProperty
    private Variant variant = new Variant();
}
