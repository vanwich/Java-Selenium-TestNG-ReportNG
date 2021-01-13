/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;

import java.util.List;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

/**
 * @Author: Vanwich
 * @Date: 7/15/2020
 */
public class PackageCdValuesRes extends DXPModel implements Adjustable<PackageCdValuesRes> {
    private List<AvailableValues> availableValues;
    private List<CoveragesApplicability> coveragesApplicability;
    private List<DefaultValue> defaultValue;

    public PackageCdValuesRes(){}

    public PackageCdValuesRes(TestData td){
        super(td);
    }

    public List<AvailableValues> getAvailableValues() {
        return availableValues;
    }

    public void setAvailableValues(List<AvailableValues> availableValues) {
        this.availableValues = availableValues;
    }

    public List<CoveragesApplicability> getCoveragesApplicability() {
        return coveragesApplicability;
    }

    public void setCoveragesApplicability(List<CoveragesApplicability> coveragesApplicability) {
        this.coveragesApplicability = coveragesApplicability;
    }

    public List<DefaultValue> getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(List<DefaultValue> defaultValue) {
        this.defaultValue = defaultValue;
    }
}
