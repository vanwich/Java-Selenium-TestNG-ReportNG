/* Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package rest.service.policy.modeling.auto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;
import help.ws.rest.model.Equality;
import rest.service.policy.modeling.Address;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vehicle extends DXPModel implements Adjustable<Vehicle> {
    @Equality(include = false)
    private String oid;
    private String vin;
    private String make;
    private String model;
    private String year;
    private String type;
    private String registrationNo;
    private String vehicleUsageCd;
    private Double marketValue;
    private Double costNew;
    private Integer distancePerWeek;
    private String estimatedAnnualDistanceCd;
    private String noVinReasonCd;
    private String performanceCd;
    private String otherManufacturer;
    private String otherSeries;
    private String series;
    private String registeredStateCd;
    private String manufactureYear;
    private String airBagStatusCd;
    private String antiLockBrakeCd;
    private String armoredInd;
    private Boolean automaticBeltsInd;
    private String daytimeRunningLampsInd;
    private String displacement;
    private String engineSerialNumber;
    private String fuelTypeCd;
    private String numberOfPassenger;
    private Boolean recoveryDeviceInd;
    private String registrationDate;
    private String securityOptionsCd;
    private String vehTypeCd;
    private Address garagingAddress;
    private RegisteredOwner registeredOwner;
    private List<VehicleCoverage> coverages;
    private String underwritingReason;

    public Vehicle(TestData testData) {
        super(testData);
    }

    public Vehicle() {
    }

    public String getVin() {
        return vin;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public Vehicle setYear(String year) {
        this.year = year;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getVehicleUsageCd() {
        return vehicleUsageCd;
    }

    public void setVehicleUsageCd(String vehicleUsageCd) {
        this.vehicleUsageCd = vehicleUsageCd;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }

    public Double getCostNew() {
        return costNew;
    }

    public void setCostNew(Double costNew) {
        this.costNew = costNew;
    }

    public Integer getDistancePerWeek() {
        return distancePerWeek;
    }

    public void setDistancePerWeek(Integer distancePerWeek) {
        this.distancePerWeek = distancePerWeek;
    }

    public String getEstimatedAnnualDistanceCd() {
        return estimatedAnnualDistanceCd;
    }

    public void setEstimatedAnnualDistanceCd(String estimatedAnnualDistanceCd) {
        this.estimatedAnnualDistanceCd = estimatedAnnualDistanceCd;
    }

    public String getNoVinReasonCd() {
        return noVinReasonCd;
    }

    public void setNoVinReasonCd(String noVinReasonCd) {
        this.noVinReasonCd = noVinReasonCd;
    }

    public String getPerformanceCd() {
        return performanceCd;
    }

    public void setPerformanceCd(String performanceCd) {
        this.performanceCd = performanceCd;
    }

    public String getOtherManufacturer() {
        return otherManufacturer;
    }

    public void setOtherManufacturer(String otherManufacturer) {
        this.otherManufacturer = otherManufacturer;
    }

    public String getOtherSeries() {
        return otherSeries;
    }

    public void setOtherSeries(String otherSeries) {
        this.otherSeries = otherSeries;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getRegisteredStateCd() {
        return registeredStateCd;
    }

    public void setRegisteredStateCd(String registeredStateCd) {
        this.registeredStateCd = registeredStateCd;
    }

    public String getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public Address getGaragingAddress() {
        return garagingAddress;
    }

    public void setGaragingAddress(Address garagingAddress) {
        this.garagingAddress = garagingAddress;
    }

    public RegisteredOwner getRegisteredOwner() {
        return registeredOwner;
    }

    public void setRegisteredOwner(RegisteredOwner registeredOwner) {
        this.registeredOwner = registeredOwner;
    }

    public List<VehicleCoverage> getCoverages() {
        return coverages;
    }

    public void setCoverages(List<VehicleCoverage> coverages) {
        this.coverages = coverages;
    }

    public String getAirBagStatusCd() {
        return airBagStatusCd;
    }

    public void setAirBagStatusCd(String airBagStatusCd) {
        this.airBagStatusCd = airBagStatusCd;
    }

    public String getAntiLockBrakeCd() {
        return antiLockBrakeCd;
    }

    public void setAntiLockBrakeCd(String antiLockBrakeCd) {
        this.antiLockBrakeCd = antiLockBrakeCd;
    }

    public String getArmoredInd() {
        return armoredInd;
    }

    public void setArmoredInd(String armoredInd) {
        this.armoredInd = armoredInd;
    }

    public Boolean getAutomaticBeltsInd() {
        return automaticBeltsInd;
    }

    public void setAutomaticBeltsInd(Boolean automaticBeltsInd) {
        this.automaticBeltsInd = automaticBeltsInd;
    }

    public String getDaytimeRunningLampsInd() {
        return daytimeRunningLampsInd;
    }

    public void setDaytimeRunningLampsInd(String daytimeRunningLampsInd) {
        this.daytimeRunningLampsInd = daytimeRunningLampsInd;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getEngineSerialNumber() {
        return engineSerialNumber;
    }

    public void setEngineSerialNumber(String engineSerialNumber) {
        this.engineSerialNumber = engineSerialNumber;
    }

    public String getFuelTypeCd() {
        return fuelTypeCd;
    }

    public void setFuelTypeCd(String fuelTypeCd) {
        this.fuelTypeCd = fuelTypeCd;
    }

    public String getNumberOfPassenger() {
        return numberOfPassenger;
    }

    public void setNumberOfPassenger(String numberOfPassenger) {
        this.numberOfPassenger = numberOfPassenger;
    }

    public Boolean getRecoveryDeviceInd() {
        return recoveryDeviceInd;
    }

    public void setRecoveryDeviceInd(Boolean recoveryDeviceInd) {
        this.recoveryDeviceInd = recoveryDeviceInd;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public Vehicle setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public String getSecurityOptionsCd() {
        return securityOptionsCd;
    }

    public void setSecurityOptionsCd(String securityOptionsCd) {
        this.securityOptionsCd = securityOptionsCd;
    }

    public String getVehTypeCd() {
        return vehTypeCd;
    }

    public void setVehTypeCd(String vehTypeCd) {
        this.vehTypeCd = vehTypeCd;
    }

    public String getUnderwritingReason() {
        return underwritingReason;
    }

    public void setUnderwritingReason(String underwritingReason) {
        this.underwritingReason = underwritingReason;
    }
}
