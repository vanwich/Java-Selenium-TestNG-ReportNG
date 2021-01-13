/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vehicles extends DXPModel implements Adjustable<Vehicles> {

    private String vehIdentificationNo;
    private String manufacturer;
    private String series;
    private String marketValue;
    private String vehicleCategoryCd;
    private String displacement;
    private String numberOfPassenger;
    private Double vehicleTonnages;
    private String importInd;
    private String highValueLuxuryInd;
    private String physicalDamageCode;
    private String theftCode;
    private PointDiff pointDiff;
    private TheftCoefficient theftCoefficient;


    public Vehicles(){}

    public String getVehIdentificationNo() {
        return vehIdentificationNo;
    }

    public void setVehIdentificationNo(String vehIdentificationNo) {
        this.vehIdentificationNo = vehIdentificationNo;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    public String getVehicleCategoryCd() {
        return vehicleCategoryCd;
    }

    public void setVehicleCategoryCd(String vehicleCategoryCd) {
        this.vehicleCategoryCd = vehicleCategoryCd;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getNumberOfPassenger() {
        return numberOfPassenger;
    }

    public void setNumberOfPassenger(String numberOfPassenger) {
        this.numberOfPassenger = numberOfPassenger;
    }

    public Double getVehicleTonnages() {
        return vehicleTonnages;
    }

    public void setVehicleTonnages(Double vehicleTonnages) {
        this.vehicleTonnages = vehicleTonnages;
    }

    public String getImportInd() {
        return importInd;
    }

    public void setImportInd(String importInd) {
        this.importInd = importInd;
    }

    public String getHighValueLuxuryInd() {
        return highValueLuxuryInd;
    }

    public void setHighValueLuxuryInd(String highValueLuxuryInd) {
        this.highValueLuxuryInd = highValueLuxuryInd;
    }

    public String getPhysicalDamageCode() {
        return physicalDamageCode;
    }

    public void setPhysicalDamageCode(String physicalDamageCode) {
        this.physicalDamageCode = physicalDamageCode;
    }

    public String getTheftCode() {
        return theftCode;
    }

    public void setTheftCode(String theftCode) {
        this.theftCode = theftCode;
    }

    public PointDiff getPointDiff() {
        return pointDiff;
    }

    public void setPointDiff(PointDiff pointDiff) {
        this.pointDiff = pointDiff;
    }

    public TheftCoefficient getTheftCoefficient() {
        return theftCoefficient;
    }

    public void setTheftCoefficient(TheftCoefficient theftCoefficient) {
        this.theftCoefficient = theftCoefficient;
    }
}
