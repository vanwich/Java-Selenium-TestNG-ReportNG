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
public class TheftCoefficient extends DXPModel implements Adjustable<TheftCoefficient> {
    private String stft;
    private String itft;

    public TheftCoefficient(){}

    public String getStft() {
        return stft;
    }

    public void setStft(String stft) {
        this.stft = stft;
    }

    public String getItft() {
        return itft;
    }

    public void setItft(String itft) {
        this.itft = itft;
    }
}
