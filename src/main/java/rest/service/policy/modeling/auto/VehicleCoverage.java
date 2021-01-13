/* Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package rest.service.policy.modeling.auto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleCoverage extends DXPModel implements Adjustable<VehicleCoverage> {
    private String id;
    private String coverageDesc;
    private String deductibleAmount;
    private String combinedDeductibleAmount;
    private String limitAmount;
    private String combinedLimitAmount;
    private String unitNumberOfLimit;
    private String deductibleTypeCd;
    private String referenceName;
    private String additionalLimitAmount;
    private String extendLimitAmount1;
    private String paymentPct;
    private String purposeOfUse;
    private String shippingType;
    private String coverage18Type;
    private String insuranceAcceptanceType;
    private Integer theftMaxPaymentDays;
    private String extendLimitAmount2;
    private String extendLimitAmount3;
    private String deductibleAmount2;
    private String deductibleAmount3;


    public VehicleCoverage(TestData testData) {
        super(testData);
    }

    public VehicleCoverage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverageDesc() {
        return coverageDesc;
    }

    public void setCoverageDesc(String coverageDesc) {
        this.coverageDesc = coverageDesc;
    }

    public String getDeductibleAmount() {
        return deductibleAmount;
    }

    public void setDeductibleAmount(String deductibleAmount) {
        this.deductibleAmount = deductibleAmount;
    }

    public String getCombinedDeductibleAmount() {
        return combinedDeductibleAmount;
    }

    public void setCombinedDeductibleAmount(String combinedDeductibleAmount) {
        this.combinedDeductibleAmount = combinedDeductibleAmount;
    }

    public String getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(String limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getCombinedLimitAmount() {
        return combinedLimitAmount;
    }

    public void setCombinedLimitAmount(String combinedLimitAmount) {
        this.combinedLimitAmount = combinedLimitAmount;
    }

    public String getUnitNumberOfLimit() {
        return unitNumberOfLimit;
    }

    public void setUnitNumberOfLimit(String unitNumberOfLimit) {
        this.unitNumberOfLimit = unitNumberOfLimit;
    }

    public String getDeductibleTypeCd() {
        return deductibleTypeCd;
    }

    public void setDeductibleTypeCd(String deductibleTypeCd) {
        this.deductibleTypeCd = deductibleTypeCd;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getAdditionalLimitAmount() {
        return additionalLimitAmount;
    }

    public void setAdditionalLimitAmount(String additionalLimitAmount) {
        this.additionalLimitAmount = additionalLimitAmount;
    }

    public String getExtendLimitAmount1() {
        return extendLimitAmount1;
    }

    public void setExtendLimitAmount1(String extendLimitAmount1) {
        this.extendLimitAmount1 = extendLimitAmount1;
    }

    public String getPaymentPct() {
        return paymentPct;
    }

    public void setPaymentPct(String paymentPct) {
        this.paymentPct = paymentPct;
    }

    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    public void setPurposeOfUse(String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getCoverage18Type() {
        return coverage18Type;
    }

    public void setCoverage18Type(String coverage18Type) {
        this.coverage18Type = coverage18Type;
    }

    public String getInsuranceAcceptanceType() {
        return insuranceAcceptanceType;
    }

    public void setInsuranceAcceptanceType(String insuranceAcceptanceType) {
        this.insuranceAcceptanceType = insuranceAcceptanceType;
    }

    public Integer getTheftMaxPaymentDays() {
        return theftMaxPaymentDays;
    }

    public void setTheftMaxPaymentDays(Integer theftMaxPaymentDays) {
        this.theftMaxPaymentDays = theftMaxPaymentDays;
    }

    public String getExtendLimitAmount2() {
        return extendLimitAmount2;
    }

    public void setExtendLimitAmount2(String extendLimitAmount2) {
        this.extendLimitAmount2 = extendLimitAmount2;
    }

    public String getExtendLimitAmount3() {
        return extendLimitAmount3;
    }

    public void setExtendLimitAmount3(String extendLimitAmount3) {
        this.extendLimitAmount3 = extendLimitAmount3;
    }

    public String getDeductibleAmount2() {
        return deductibleAmount2;
    }

    public void setDeductibleAmount2(String deductibleAmount2) {
        this.deductibleAmount2 = deductibleAmount2;
    }

    public String getDeductibleAmount3() {
        return deductibleAmount3;
    }

    public void setDeductibleAmount3(String deductibleAmount3) {
        this.deductibleAmount3 = deductibleAmount3;
    }
}
