package ru.moretech.moretech_server.Entities.calculatorEntities.calculate;

public class Ranges {
    private Cost cost;
    private InitialFee initialFee;
    private ResidualPayment residualPayment;
    private Term term;

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public InitialFee getInitialFee() {
        return initialFee;
    }

    public void setInitialFee(InitialFee initialFee) {
        this.initialFee = initialFee;
    }

    public ResidualPayment getResidualPayment() {
        return residualPayment;
    }

    public void setResidualPayment(ResidualPayment residualPayment) {
        this.residualPayment = residualPayment;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
