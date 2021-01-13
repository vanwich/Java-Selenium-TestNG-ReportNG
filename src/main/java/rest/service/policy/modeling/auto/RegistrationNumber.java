/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;

import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

/**
 * @Author: Vanwich
 * @Date: 6/24/2020
 */
public class RegistrationNumber extends DXPModel implements Adjustable<RegistrationNumber> {

    private String registrationNo;
    private String vehicleCategoryCd;
    private String displacement;


    public RegistrationNumber(){
    }

    public RegistrationNumber(TestData td){
        super(td);
    }

    public static RegistrationNumber getDefault(){
        return new RegistrationNumber().setDisplacement("1170").setRegistrationNo("KL-90").setVehicleCategoryCd("32");
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public RegistrationNumber setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
        return this;
    }

    public String getVehicleCategoryCd() {
        return vehicleCategoryCd;
    }

    public RegistrationNumber setVehicleCategoryCd(String vehicleCategoryCd) {
        this.vehicleCategoryCd = vehicleCategoryCd;
        return this;
    }

    public String getDisplacement() {
        return displacement;
    }

    public RegistrationNumber setDisplacement(String displacement) {
        this.displacement = displacement;
        return this;
    }
}
