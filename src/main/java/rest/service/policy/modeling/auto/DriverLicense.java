/* Copyright © 2017 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent. */
package rest.service.policy.modeling.auto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;
import help.ws.rest.model.Equality;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverLicense extends DXPModel implements Adjustable<DriverLicense> {
    @Equality(include = false)
    private String oid;
    private String licenseTypeCd;
    private Integer ageFirstLicensed;
    private Boolean permitBeforeLicenseInd;
    private String licenseDate;
    private String licenseStateProvCd;
    private String licensePermitNumber;
    private String licenseStatusCd;
    private String licenseClassCd;

    public DriverLicense(TestData testData) {
        super(testData);
    }

    public DriverLicense() {
    }

    public String getLicenseTypeCd() {
        return licenseTypeCd;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setLicenseTypeCd(String licenseTypeCd) {
        this.licenseTypeCd = licenseTypeCd;
    }

    public Integer getAgeFirstLicensed() {
        return ageFirstLicensed;
    }

    public void setAgeFirstLicensed(Integer ageFirstLicensed) {
        this.ageFirstLicensed = ageFirstLicensed;
    }

    public Boolean getPermitBeforeLicenseInd() {
        return permitBeforeLicenseInd;
    }

    public void setPermitBeforeLicenseInd(Boolean permitBeforeLicenseInd) {
        this.permitBeforeLicenseInd = permitBeforeLicenseInd;
    }

    public String getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(String licenseDate) {
        this.licenseDate = licenseDate;
    }

    public String getLicenseStateProvCd() {
        return licenseStateProvCd;
    }

    public void setLicenseStateProvCd(String licenseStateProvCd) {
        this.licenseStateProvCd = licenseStateProvCd;
    }

    public String getLicensePermitNumber() {
        return licensePermitNumber;
    }

    public void setLicensePermitNumber(String licensePermitNumber) {
        this.licensePermitNumber = licensePermitNumber;
    }

    public String getLicenseStatusCd() {
        return licenseStatusCd;
    }

    public void setLicenseStatusCd(String licenseStatusCd) {
        this.licenseStatusCd = licenseStatusCd;
    }

    public String getLicenseClassCd() {
        return licenseClassCd;
    }

    public void setLicenseClassCd(String licenseClassCd) {
        this.licenseClassCd = licenseClassCd;
    }
}
