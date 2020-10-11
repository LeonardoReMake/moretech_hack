package ru.smirnov.test.moretechapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import androidx.lifecycle.ViewModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoanResult extends ViewModel {

    public double contractRate;

    public int kaskoCost;

    public double lastPayment;

    public double loanAmount;

    public int payment;

    public double subsidy;

    public int term;

    @JsonProperty("result")
    public void unPackedResult(Map<String, Object> result) {
        if (result.get("contractRate") != null)
            this.contractRate = (double) result.get("contractRate");
        if (result.get("kaskoCost") != null)
            this.kaskoCost = (int) result.get("kaskoCost");
        if (result.get("residualPayment") != null)
            this.lastPayment = (double) result.get("residualPayment");
        if (result.get("loanAmount") != null)
            this.loanAmount = (double) result.get("loanAmount");
        if (result.get("subsidy") != null)
            this.subsidy = (double) result.get("subsidy");
        if (result.get("term") != null)
            this.term = (int) result.get("term");
        if (result.get("payment") != null)
            this.payment = (int) result.get("payment");
    }

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

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
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
