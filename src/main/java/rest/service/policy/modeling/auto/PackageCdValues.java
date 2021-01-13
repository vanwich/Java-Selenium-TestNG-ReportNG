/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;

import help.ws.rest.model.DXPModel;

/**
 * @Author: Vanwich
 * @Date: 7/15/2020
 */
public class PackageCdValues extends DXPModel {
    private String typeOfPolicyCd;
    private String producerCd;
    private String vehicleCategoryCd;
    private String vehicleUsageCd;

    public PackageCdValues(){}

    public String getTypeOfPolicyCd() {
        return typeOfPolicyCd;
    }

    public PackageCdValues setTypeOfPolicyCd(String typeOfPolicyCd) {
        this.typeOfPolicyCd = typeOfPolicyCd;
        return this;
    }

    public String getProducerCd() {
        return producerCd;
    }

    public PackageCdValues setProducerCd(String producerCd) {
        this.producerCd = producerCd;
        return this;
    }

    public String getVehicleCategoryCd() {
        return vehicleCategoryCd;
    }

    public PackageCdValues setVehicleCategoryCd(String vehicleCategoryCd) {
        this.vehicleCategoryCd = vehicleCategoryCd;
        return this;
    }

    public String getVehicleUsageCd() {
        return vehicleUsageCd;
    }

    public PackageCdValues setVehicleUsageCd(String vehicleUsageCd) {
        this.vehicleUsageCd = vehicleUsageCd;
        return this;
    }
}
