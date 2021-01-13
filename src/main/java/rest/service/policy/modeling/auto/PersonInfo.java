/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import help.data.TestData;
import help.utils.DateUtils;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;
import help.ws.rest.model.Equality;
import rest.service.policy.modeling.Address;
import rest.service.policy.modeling.Email;
import rest.service.policy.modeling.Phone;

/**
 * @Author: Vanwich
 * @Date: 5/20/2020
 */
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonInfo extends DXPModel implements Adjustable<PersonInfo> {
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
    private String relationshipToPolicyHolder;
    private String relationshipToPolicyHolderDesc;
    private String legalIdentification;
    private Address address;
    private List<Phone> phones;
    private List<Email> emails;

    public PersonInfo(TestData td){
        super(td);
    }

    public PersonInfo(){}

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

    public String getRelationshipToPolicyHolder() {
        return relationshipToPolicyHolder;
    }

    public void setRelationshipToPolicyHolder(String relationshipToPolicyHolder) {
        this.relationshipToPolicyHolder = relationshipToPolicyHolder;
    }

    public String getLegalIdentification() {
        return legalIdentification;
    }

    public void setLegalIdentification(String legalIdentification) {
        this.legalIdentification = legalIdentification;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public String getRelationshipToPolicyHolderDesc() {
        return relationshipToPolicyHolderDesc;
    }

    public void setRelationshipToPolicyHolderDesc(String relationshipToPolicyHolderDesc) {
        this.relationshipToPolicyHolderDesc = relationshipToPolicyHolderDesc;
    }
}
