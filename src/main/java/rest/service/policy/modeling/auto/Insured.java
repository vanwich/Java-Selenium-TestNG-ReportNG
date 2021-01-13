/* Copyright © 2017 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent. */
package rest.service.policy.modeling.auto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;
import help.ws.rest.model.Equality;

@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Insured extends DXPModel implements Adjustable<Insured> {
    @Equality(include = false)
    private String oid;
    private Boolean primaryInsuredInd;
    private String additionalInsured;
    private String creditScoreTypeCd;
    private String carrierCd;
    private Boolean hadPriorClaims;
    private PersonInfo personInfo;
    private CorporationInfo corporationInfo;

    public Insured(TestData td){
        super(td);
    }

    public Insured(){}

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Boolean getPrimaryInsuredInd() {
        return primaryInsuredInd;
    }

    public void setPrimaryInsuredInd(Boolean primaryInsuredInd) {
        this.primaryInsuredInd = primaryInsuredInd;
    }

    public String getAdditionalInsured() {
        return additionalInsured;
    }

    public void setAdditionalInsured(String additionalInsured) {
        this.additionalInsured = additionalInsured;
    }

    public String getCreditScoreTypeCd() {
        return creditScoreTypeCd;
    }

    public void setCreditScoreTypeCd(String creditScoreTypeCd) {
        this.creditScoreTypeCd = creditScoreTypeCd;
    }

    public String getCarrierCd() {
        return carrierCd;
    }

    public void setCarrierCd(String carrierCd) {
        this.carrierCd = carrierCd;
    }

    public Boolean getHadPriorClaims() {
        return hadPriorClaims;
    }

    public void setHadPriorClaims(Boolean hadPriorClaims) {
        this.hadPriorClaims = hadPriorClaims;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public CorporationInfo getCorporationInfo() {
        return corporationInfo;
    }

    public void setCorporationInfo(CorporationInfo corporationInfo) {
        this.corporationInfo = corporationInfo;
    }
}
