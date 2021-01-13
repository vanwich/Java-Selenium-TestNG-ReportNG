/* Copyright © 2017 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent. */
package rest.service.policy.modeling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;
import help.ws.rest.model.Equality;

@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Phone extends DXPModel implements Adjustable<Phone> {
    @Equality(include = false)
    private String oid;
    private String phone;
    private String phoneTypeCd;

    public Phone(TestData testData) {
        super(testData);
    }

    public Phone() {
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneTypeCd() {
        return phoneTypeCd;
    }

    public void setPhoneTypeCd(String phoneTypeCd) {
        this.phoneTypeCd = phoneTypeCd;
    }
}
