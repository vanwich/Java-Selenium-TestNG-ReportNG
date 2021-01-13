/* Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package rest.service.policy.modeling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuoteRate extends DXPModel implements Adjustable<QuoteRate> {
    private String transactionEffectiveDate;
    private String policyNumber;
    private String totalPremium;
    private String annualPremium;
    private String monthlyPremium;
    private String termPremium;
    private String premiumChange;
    private String discount;
    private String taxes;
    private String fees;
    private String currencyCode;

    public QuoteRate(TestData testData) {
        super(testData);
    }

    public QuoteRate() {
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getTransactionEffectiveDate() {
        return transactionEffectiveDate;
    }

    public void setTransactionEffectiveDate(String transactionEffectiveDate) {
        this.transactionEffectiveDate = transactionEffectiveDate;
    }

    public String getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(String totalPremium) {
        this.totalPremium = totalPremium;
    }

    public String getAnnualPremium() {
        return annualPremium;
    }

    public void setAnnualPremium(String annualPremium) {
        this.annualPremium = annualPremium;
    }

    public String getMonthlyPremium() {
        return monthlyPremium;
    }

    public void setMonthlyPremium(String monthlyPremium) {
        this.monthlyPremium = monthlyPremium;
    }

    public String getTermPremium() {
        return termPremium;
    }

    public void setTermPremium(String termPremium) {
        this.termPremium = termPremium;
    }

    public String getPremiumChange() {
        return premiumChange;
    }

    public void setPremiumChange(String premiumChange) {
        this.premiumChange = premiumChange;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
