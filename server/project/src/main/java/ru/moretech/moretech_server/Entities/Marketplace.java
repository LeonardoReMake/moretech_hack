package ru.moretech.moretech_server.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Marketplace {

    @JsonProperty
    private CarBrand[] list;
}
