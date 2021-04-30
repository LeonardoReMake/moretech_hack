package ru.smirnov.test.moretechapp.models;

import androidx.lifecycle.ViewModel;

public class UserLoan extends ViewModel {

    public String[] clientTypes = {"ac43d7e4-cd8c-4f6f-b18a-5ccbc1356f75"};
    public double cost = 1000000;
    public double initialFee = 200000;
    public int kaskoValue;
    public String language = "ru-RU";
    public double residualPayment = 100000;
    public String settingsName = "Haval";
    public String[] specialConditions = {"57ba0183-5988-4137-86a6-3d30a4ed8dc9"};
    public int term = 4;

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
