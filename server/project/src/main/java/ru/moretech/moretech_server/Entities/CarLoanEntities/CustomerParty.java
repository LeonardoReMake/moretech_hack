package ru.moretech.moretech_server.Entities.CarLoanEntities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerParty {
    private String email;
    private int income_amount;
    @JsonProperty
    private Person person;
    private String phone;
}
