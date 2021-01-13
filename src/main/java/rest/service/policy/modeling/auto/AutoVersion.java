/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

/**
 * @Author: Vanwich
 * @Date: 5/28/2020
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AutoVersion extends DXPModel implements Adjustable<AutoVersion> {

    private String versionDesc;
    private AutoQuote quote;

    public AutoVersion(){}

    public AutoVersion(TestData td){
        super(td);
    }

    public static AutoVersion getDefaultData(){
        return new AutoVersion().setVersionDesc("just create new version not update quote");
    }

    public static AutoVersion creteVersionAndUpdateQuote(AutoQuote autoQuote){
        return new AutoVersion().setVersionDesc("create new version and update quote").setQuote(autoQuote);
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public AutoVersion setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
        return this;
    }

    public AutoQuote getQuote() {
        return quote;
    }

    public AutoVersion setQuote(AutoQuote quote) {
        this.quote = quote;
        return this;
    }
}
