/* Copyright © 2017 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent. */
package rest.service.policy.modeling.auto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisteredOwner extends DXPModel implements Adjustable<RegisteredOwner> {
    private String firstName;
    private String lastName;
    private String middleName;
    private String registrationTypeCd;

    public RegisteredOwner(TestData testData) {
        super(testData);
    }

    public RegisteredOwner() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getRegistrationTypeCd() {
        return registrationTypeCd;
    }

    public void setRegistrationTypeCd(String registrationTypeCd) {
        this.registrationTypeCd = registrationTypeCd;
    }
}
