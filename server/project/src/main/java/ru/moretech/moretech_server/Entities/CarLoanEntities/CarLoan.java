package ru.moretech.moretech_server.Entities.CarLoanEntities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CarLoan {
    private String comment;
    @JsonProperty
    private CustomerParty customer_party;
    private String datetime;
    private double interest_rate;
    private int requested_amount;
    private int requested_term;
    private String trade_mark;
    private int vehicle_cost;
}
