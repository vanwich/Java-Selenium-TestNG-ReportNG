/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;

import help.ws.rest.model.DXPModel;

/**
 * @Author: Vanwich
 * @Date: 7/21/2020
 */
public class QuotePolicy extends DXPModel {
    private String policyNumber;
    private String relatedVoluntaryPolicyNumber;
    private String relatedCompulsoryPolicyNumber;
    private String customerNumber;
    private String customerName;
    private String brandDesc;
    private String effectiveDate;
    private String expirationDate;
    private String transactionEffectiveDate;
    private String transactionTypeCd;
    private String transactionType;
    private String policyStatusCd;
    private String policyStatusDesc;
    private String quoteStatusCd;
    private String revisionNumber;
    private String pendingRevisionNumber;
    private String productCode;
    private String productVersion;
    private String totalPremium;
    private String imported;
    private String typeOfPolicyCd;
    private String vehicleCategoryCd;
    private String vehicleTypeCd;
    private String registrationNo;
    private String expired;
    private String timedPolicyStatusCd;
    private String producerCd;
    private String producerDesc;
    private String subProducerCd;
    private String producerMarketChannelCd;
    private String bundleId;
    private String groupCode;
    private String description;
    private String partyRole;
    private String partyRoleDesc;
    private String partyName;
    private String region;
    private Boolean hoanChanneInd;
    private String preConversionPolicyNumber;

    public QuotePolicy(){}

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getImported() {
        return imported;
    }

    public void setImported(String imported) {
        this.imported = imported;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyRole() {
        return partyRole;
    }

    public void setPartyRole(String partyRole) {
        this.partyRole = partyRole;
    }

    public String getPartyRoleDesc() {
        return partyRoleDesc;
    }

    public void setPartyRoleDesc(String partyRoleDesc) {
        this.partyRoleDesc = partyRoleDesc;
    }

    public String getPendingRevisionNumber() {
        return pendingRevisionNumber;
    }

    public void setPendingRevisionNumber(String pendingRevisionNumber) {
        this.pendingRevisionNumber = pendingRevisionNumber;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyStatusCd() {
        return policyStatusCd;
    }

    public void setPolicyStatusCd(String policyStatusCd) {
        this.policyStatusCd = policyStatusCd;
    }

    public String getPolicyStatusDesc() {
        return policyStatusDesc;
    }

    public void setPolicyStatusDesc(String policyStatusDesc) {
        this.policyStatusDesc = policyStatusDesc;
    }

    public String getProducerCd() {
        return producerCd;
    }

    public void setProducerCd(String producerCd) {
        this.producerCd = producerCd;
    }

    public String getProducerDesc() {
        return producerDesc;
    }

    public void setProducerDesc(String producerDesc) {
        this.producerDesc = producerDesc;
    }

    public String getProducerMarketChannelCd() {
        return producerMarketChannelCd;
    }

    public void setProducerMarketChannelCd(String producerMarketChannelCd) {
        this.producerMarketChannelCd = producerMarketChannelCd;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public String getSubProducerCd() {
        return subProducerCd;
    }

    public void setSubProducerCd(String subProducerCd) {
        this.subProducerCd = subProducerCd;
    }

    public String getTimedPolicyStatusCd() {
        return timedPolicyStatusCd;
    }

    public void setTimedPolicyStatusCd(String timedPolicyStatusCd) {
        this.timedPolicyStatusCd = timedPolicyStatusCd;
    }

    public String getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(String totalPremium) {
        this.totalPremium = totalPremium;
    }

    public String getTransactionEffectiveDate() {
        return transactionEffectiveDate;
    }

    public void setTransactionEffectiveDate(String transactionEffectiveDate) {
        this.transactionEffectiveDate = transactionEffectiveDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionTypeCd() {
        return transactionTypeCd;
    }

    public void setTransactionTypeCd(String transactionTypeCd) {
        this.transactionTypeCd = transactionTypeCd;
    }

    public String getTypeOfPolicyCd() {
        return typeOfPolicyCd;
    }

    public void setTypeOfPolicyCd(String typeOfPolicyCd) {
        this.typeOfPolicyCd = typeOfPolicyCd;
    }

    public String getVehicleCategoryCd() {
        return vehicleCategoryCd;
    }

    public void setVehicleCategoryCd(String vehicleCategoryCd) {
        this.vehicleCategoryCd = vehicleCategoryCd;
    }

    public String getVehicleTypeCd() {
        return vehicleTypeCd;
    }

    public void setVehicleTypeCd(String vehicleTypeCd) {
        this.vehicleTypeCd = vehicleTypeCd;
    }

    public String getRelatedVoluntaryPolicyNumber() {
        return relatedVoluntaryPolicyNumber;
    }

    public void setRelatedVoluntaryPolicyNumber(String relatedVoluntaryPolicyNumber) {
        this.relatedVoluntaryPolicyNumber = relatedVoluntaryPolicyNumber;
    }

    public String getRelatedCompulsoryPolicyNumber() {
        return relatedCompulsoryPolicyNumber;
    }

    public void setRelatedCompulsoryPolicyNumber(String relatedCompulsoryPolicyNumber) {
        this.relatedCompulsoryPolicyNumber = relatedCompulsoryPolicyNumber;
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }

    public String getQuoteStatusCd() {
        return quoteStatusCd;
    }

    public void setQuoteStatusCd(String quoteStatusCd) {
        this.quoteStatusCd = quoteStatusCd;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getHoanChanneInd() {
        return hoanChanneInd;
    }

    public void setHoanChanneInd(Boolean hoanChanneInd) {
        this.hoanChanneInd = hoanChanneInd;
    }

    public String getPreConversionPolicyNumber() {
        return preConversionPolicyNumber;
    }

    public void setPreConversionPolicyNumber(String preConversionPolicyNumber) {
        this.preConversionPolicyNumber = preConversionPolicyNumber;
    }
}
