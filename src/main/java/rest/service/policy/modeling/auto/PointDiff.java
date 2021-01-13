/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

/**
 * @Author: Vanwich
 * @Date: 9/10/2020
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointDiff extends DXPModel implements Adjustable<PointDiff> {
    private String scom;
    private String icom;

    public PointDiff(){}

    public String getScom() {
        return scom;
    }

    public void setScom(String scom) {
        this.scom = scom;
    }

    public String getIcom() {
        return icom;
    }

    public void setIcom(String icom) {
        this.icom = icom;
    }
}
