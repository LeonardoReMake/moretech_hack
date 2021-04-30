package ru.moretech.moretech_server.Entities.calculatorEntities.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Variant {
    private String id;
    private String language;
    private String name;
}
