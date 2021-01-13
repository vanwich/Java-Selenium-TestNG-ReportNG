/* Copyright © 2017 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent. */
package rest.service.policy.modeling.auto;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import help.data.TestData;
import help.utils.DateUtils;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;
import help.ws.rest.model.Equality;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Driver extends DXPModel implements Adjustable<Driver> {
    @Equality(include = false)
    private String oid;
    private String firstName;
    private String middleName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.YYYY_MM_DD)
    private LocalDate dateOfBirth;
    private String genderCd;
    private String maritalStatusCd;
    private String employmentStatusCd;
    private String legalIdentification;
    private String relationshipToInsuredCd;
    private String relationshipToInsuredDesc;
    private String driverTypeCd;
    private String driverTypeDesc;
    private String filingNeed;
    private String driverHDEBCOAFQuestionAnswer;
    private String driverPersonOid;
    private List<DriverLicense> driverLicenses;

    //Add
    private String phone;
    private boolean book;
    private List<Beneficiaries> beneficiaries;


    public Driver(TestData testData) {
        super(testData);
    }

    public Driver() {
    }

    public String getRelationshipToInsuredCd() {
        return relationshipToInsuredCd;
    }

    public void setRelationshipToInsuredCd(String relationshipToInsuredCd) {
        this.relationshipToInsuredCd = relationshipToInsuredCd;
    }

    public String getDriverTypeCd() {
        return driverTypeCd;
    }

    public void setDriverTypeCd(String driverTypeCd) {
        this.driverTypeCd = driverTypeCd;
    }

    public List<DriverLicense> getDriverLicenses() {
        return driverLicenses;
    }

    public void setDriverLicenses(List<DriverLicense> driverLicenses) {
        this.driverLicenses = driverLicenses;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGenderCd() {
        return genderCd;
    }

    public void setGenderCd(String genderCd) {
        this.genderCd = genderCd;
    }

    public String getMaritalStatusCd() {
        return maritalStatusCd;
    }

    public void setMaritalStatusCd(String maritalStatusCd) {
        this.maritalStatusCd = maritalStatusCd;
    }

    public String getEmploymentStatusCd() {
        return employmentStatusCd;
    }

    public void setEmploymentStatusCd(String employmentStatusCd) {
        this.employmentStatusCd = employmentStatusCd;
    }

    public String getLegalIdentification() {
        return legalIdentification;
    }

    public void setLegalIdentification(String legalIdentification) {
        this.legalIdentification = legalIdentification;
    }

    public String getFilingNeed() {
        return filingNeed;
    }

    public void setFilingNeed(String filingNeed) {
        this.filingNeed = filingNeed;
    }

    public String getDriverHDEBCOAFQuestionAnswer() {
        return driverHDEBCOAFQuestionAnswer;
    }

    public void setDriverHDEBCOAFQuestionAnswer(String driverHDEBCOAFQuestionAnswer) {
        this.driverHDEBCOAFQuestionAnswer = driverHDEBCOAFQuestionAnswer;
    }

    public String getDriverPersonOid() {
        return driverPersonOid;
    }

    public void setDriverPersonOid(String driverPersonOid) {
        this.driverPersonOid = driverPersonOid;
    }

    public String getDriverTypeDesc() {
        return driverTypeDesc;
    }

    public void setDriverTypeDesc(String driverTypeDesc) {
        this.driverTypeDesc = driverTypeDesc;
    }

    public String getRelationshipToInsuredDesc() {
        return relationshipToInsuredDesc;
    }

    public void setRelationshipToInsuredDesc(String relationshipToInsuredDesc) {
        this.relationshipToInsuredDesc = relationshipToInsuredDesc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isBook() {
        return book;
    }

    public void setBook(boolean book) {
        this.book = book;
    }

    public List<Beneficiaries> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<Beneficiaries> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }
}
