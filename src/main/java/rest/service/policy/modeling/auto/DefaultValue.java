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
public class DefaultValue extends DXPModel {
    private String attribute;
    private String component;
    private String coverageDesc;
    private String defaultValue;

    public DefaultValue(){}

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
