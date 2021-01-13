/* Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package rest.service.policy.modeling.auto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import help.data.TestData;
import help.utils.TestDataHolder;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AutoQuote extends DXPModel implements Adjustable<AutoQuote> {
    private String policyNumber;
    private String customerNumber;
    private String effectiveDate;
    private String expirationDate;
    private String transactionEffectiveDate	;
    private String policyStatusCd;
    private String transactionTypeCd;
    private String timedPolicyStatusCd;
    private Integer revisionNumber;
    private Integer pendingRevisionNumber;
    private String productCode;
    private Integer productVersion;
    private String lobCd;
    private String totalPremium;
    private String currencyCode;
    private String packageCd;
    private String imported;
    private String typeOfPolicyCd;
    private String vehicleCategoryCd;
    private String vehicleCategoryDesc;
    private String vehicleTypeCd;
    private String vehicleTypeDesc;
    private String producerCd;
    private String producerDesc;
    private String subProducerCd;
    private String producerMarketChannelCd;
    private String contractTermTypeCd;
    private String salesSource;
    private List<Insured> insureds;
    private List<Driver> drivers;
    private List<Vehicle> vehicles;
    private Boolean openTender;
    private String preConversionPolicyNumber;
    private String region;
    private String zone;
    private Boolean hoanChanneInd;
    private String relatedCompulsoryPolicyNumber;
    private String relatedVoluntaryPolicyNumber;
    private String serviceStaff;
    private String statisticalCode;
    private String underwritingReason;
    private String compInsureCertNo;
    private String transNumber;
    // add
    private String pointDiff;
    private String grade;
    private String drunkDrivingTimes;
    private String currentTermGrade;
    private Integer drunkDriveIncAmount;


    public AutoQuote(TestData testData) {
        super(testData);
    }

    public AutoQuote() {
    }

    public static AutoQuote getDefault(String customerNumber) {
        return new AutoQuote(TestDataHolder.tdAuto.getTestData("CreateQuote", "TestData")).setCustomerNumber(customerNumber);
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public AutoQuote setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
        return this;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public AutoQuote setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
        return this;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public AutoQuote setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getTransactionEffectiveDate() {
        return transactionEffectiveDate;
    }

    public void setTransactionEffectiveDate(String transactionEffectiveDate) {
        this.transactionEffectiveDate = transactionEffectiveDate;
    }

    public String getPolicyStatusCd() {
        return policyStatusCd;
    }

    public void setPolicyStatusCd(String policyStatusCd) {
        this.policyStatusCd = policyStatusCd;
    }

    public String getTransactionTypeCd() {
        return transactionTypeCd;
    }

    public void setTransactionTypeCd(String transactionTypeCd) {
        this.transactionTypeCd = transactionTypeCd;
    }

    public String getTimedPolicyStatusCd() {
        return timedPolicyStatusCd;
    }

    public void setTimedPolicyStatusCd(String timedPolicyStatusCd) {
        this.timedPolicyStatusCd = timedPolicyStatusCd;
    }

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public Integer getPendingRevisionNumber() {
        return pendingRevisionNumber;
    }

    public void setPendingRevisionNumber(Integer pendingRevisionNumber) {
        this.pendingRevisionNumber = pendingRevisionNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(Integer productVersion) {
        this.productVersion = productVersion;
    }

    public String getLobCd() {
        return lobCd;
    }

    public void setLobCd(String lobCd) {
        this.lobCd = lobCd;
    }

    public String getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(String totalPremium) {
        this.totalPremium = totalPremium;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPackageCd() {
        return packageCd;
    }

    public void setPackageCd(String packageCd) {
        this.packageCd = packageCd;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getImported() {
        return imported;
    }

    public void setImported(String imported) {
        this.imported = imported;
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

    public String getProducerCd() {
        return producerCd;
    }

    public void setProducerCd(String producerCd) {
        this.producerCd = producerCd;
    }

    public String getSubProducerCd() {
        return subProducerCd;
    }

    public void setSubProducerCd(String subProducerCd) {
        this.subProducerCd = subProducerCd;
    }

    public String getProducerMarketChannelCd() {
        return producerMarketChannelCd;
    }

    public void setProducerMarketChannelCd(String producerMarketChannelCd) {
        this.producerMarketChannelCd = producerMarketChannelCd;
    }

    public String getContractTermTypeCd() {
        return contractTermTypeCd;
    }

    public void setContractTermTypeCd(String contractTermTypeCd) {
        this.contractTermTypeCd = contractTermTypeCd;
    }

    public List<Insured> getInsureds() {
        return insureds;
    }

    public void setInsureds(List<Insured> insureds) {
        this.insureds = insureds;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public String getSalesSource() {
        return salesSource;
    }

    public void setSalesSource(String salesSource) {
        this.salesSource = salesSource;
    }

    public String getProducerDesc() {
        return producerDesc;
    }

    public void setProducerDesc(String producerDesc) {
        this.producerDesc = producerDesc;
    }

    public String getVehicleCategoryDesc() {
        return vehicleCategoryDesc;
    }

    public void setVehicleCategoryDesc(String vehicleCategoryDesc) {
        this.vehicleCategoryDesc = vehicleCategoryDesc;
    }

    public String getVehicleTypeDesc() {
        return vehicleTypeDesc;
    }

    public void setVehicleTypeDesc(String vehicleTypeDesc) {
        this.vehicleTypeDesc = vehicleTypeDesc;
    }

    public Boolean getOpenTender() {
        return openTender;
    }

    public void setOpenTender(Boolean openTender) {
        this.openTender = openTender;
    }

    public String getPreConversionPolicyNumber() {
        return preConversionPolicyNumber;
    }

    public void setPreConversionPolicyNumber(String preConversionPolicyNumber) {
        this.preConversionPolicyNumber = preConversionPolicyNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Boolean getHoanChanneInd() {
        return hoanChanneInd;
    }

    public void setHoanChanneInd(Boolean hoanChanneInd) {
        this.hoanChanneInd = hoanChanneInd;
    }

    public String getRelatedCompulsoryPolicyNumber() {
        return relatedCompulsoryPolicyNumber;
    }

    public void setRelatedCompulsoryPolicyNumber(String relatedCompulsoryPolicyNumber) {
        this.relatedCompulsoryPolicyNumber = relatedCompulsoryPolicyNumber;
    }

    public String getRelatedVoluntaryPolicyNumber() {
        return relatedVoluntaryPolicyNumber;
    }

    public void setRelatedVoluntaryPolicyNumber(String relatedVoluntaryPolicyNumber) {
        this.relatedVoluntaryPolicyNumber = relatedVoluntaryPolicyNumber;
    }

    public String getServiceStaff() {
        return serviceStaff;
    }

    public void setServiceStaff(String serviceStaff) {
        this.serviceStaff = serviceStaff;
    }

    public String getStatisticalCode() {
        return statisticalCode;
    }

    public void setStatisticalCode(String statisticalCode) {
        this.statisticalCode = statisticalCode;
    }

    public String getUnderwritingReason() {
        return underwritingReason;
    }

    public void setUnderwritingReason(String underwritingReason) {
        this.underwritingReason = underwritingReason;
    }

    public String getCompInsureCertNo() {
        return compInsureCertNo;
    }

    public void setCompInsureCertNo(String compInsureCertNo) {
        this.compInsureCertNo = compInsureCertNo;
    }

    public String getTransNumber() {
        return transNumber;
    }

    public void setTransNumber(String transNumber) {
        this.transNumber = transNumber;
    }

    public String getPointDiff() {
        return pointDiff;
    }

    public void setPointDiff(String pointDiff) {
        this.pointDiff = pointDiff;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDrunkDrivingTimes() {
        return drunkDrivingTimes;
    }

    public void setDrunkDrivingTimes(String drunkDrivingTimes) {
        this.drunkDrivingTimes = drunkDrivingTimes;
    }

    public String getCurrentTermGrade() {
        return currentTermGrade;
    }

    public void setCurrentTermGrade(String currentTermGrade) {
        this.currentTermGrade = currentTermGrade;
    }

    public Integer getDrunkDriveIncAmount() {
        return drunkDriveIncAmount;
    }

    public void setDrunkDriveIncAmount(Integer drunkDriveIncAmount) {
        this.drunkDriveIncAmount = drunkDriveIncAmount;
    }
}
