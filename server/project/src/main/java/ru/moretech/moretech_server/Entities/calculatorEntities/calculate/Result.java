package ru.moretech.moretech_server.Entities.calculatorEntities.calculate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private double contractRate;
    private int kaskoCost;
    private double lastPayment;
    private double loanAmount;
    private double payment;
    private double subsidy;
    private int term;

    public double getContractRate() {
        return contractRate;
    }

    public void setContractRate(double contractRate) {
        this.contractRate = contractRate;
    }

    public int getKaskoCost() {
        return kaskoCost;
    }

    public void setKaskoCost(int kaskoCost) {
        this.kaskoCost = kaskoCost;
    }

    public double getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(double lastPayment) {
        this.lastPayment = lastPayment;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public double getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(double subsidy) {
        this.subsidy = subsidy;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }
}
