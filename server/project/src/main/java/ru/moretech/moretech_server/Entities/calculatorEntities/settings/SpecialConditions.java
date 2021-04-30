package ru.moretech.moretech_server.Entities.calculatorEntities.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecialConditions {
    @JsonProperty
    private String[] excludingConditions;
    private String id;
    private boolean isChecked;
    private String language;
    private String name;
}
