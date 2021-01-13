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
public class CoveragesApplicability extends DXPModel {
    private String applicablitityStatus;
    private String component;
    private String coverageDesc;

    public CoveragesApplicability(){}

    public String getApplicablitityStatus() {
        return applicablitityStatus;
    }

    public void setApplicablitityStatus(String applicablitityStatus) {
        this.applicablitityStatus = applicablitityStatus;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getCoverageDesc() {
        return coverageDesc;
    }

    public void setCoverageDesc(String coverageDesc) {
        this.coverageDesc = coverageDesc;
    }
}
