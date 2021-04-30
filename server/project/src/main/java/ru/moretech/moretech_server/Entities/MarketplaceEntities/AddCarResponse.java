package ru.moretech.moretech_server.Entities.MarketplaceEntities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class AddCarResponse {
    private static final Logger LOG = LoggerFactory.getLogger(AddCarResponse.class);

    private AddCarResponse() {
    }

    private AddCarResponse(boolean isAdded) {
        this.isAdded = isAdded;
    }

    private AddCarResponse(boolean isAdded, String error) {
        this.isAdded = isAdded;
        this.error = error;
    }

    static public AddCarResponse successful() {
        return new AddCarResponse(true);
    }

    static public AddCarResponse error(String error) {
        return new AddCarResponse(false, error);
    }

    @JsonProperty
    private boolean isAdded;
    @JsonProperty
    private String error;
}
