package ru.moretech.moretech_server.Entities.calculatorEntities.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Variant {
    private String id;
    private String language;
    private String name;

    public Variant() {
    }

    public Variant(String id, String language, String name) {
        this.id = id;
        this.language = language;
        this.name = name;
    }
}
