/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;
import help.ws.rest.model.Equality;
import rest.service.policy.modeling.Email;
import rest.service.policy.modeling.Phone;
import rest.service.policy.modeling.quotes.Address;

/**
 * @Author: Vanwich
 * @Date: 5/27/2020
 */
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class CorporationInfo extends DXPModel implements Adjustable<CorporationInfo> {
    @Equality(include = false)
    private String oid;
    private String corporateName;
    private String legalRepresentative;
    private String legalIdentification;
    private String corporationTypeCd;
    private String beneficiaryOwner;
    private String relationshipToPolicyHolder;
    private Address address;
    private List<Phone> phones;
    private List<Email> emails;
    private PreferredContact preferredContact;

    public CorporationInfo(){}

    public CorporationInfo(TestData td){
        super(td);
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getLegalIdentification() {
        return legalIdentification;
    }

    public void setLegalIdentification(String legalIdentification) {
        this.legalIdentification = legalIdentification;
    }

    public String getCorporationTypeCd() {
        return corporationTypeCd;
    }

    public void setCorporationTypeCd(String corporationTypeCd) {
        this.corporationTypeCd = corporationTypeCd;
    }

    public String getBeneficiaryOwner() {
        return beneficiaryOwner;
    }

    public void setBeneficiaryOwner(String beneficiaryOwner) {
        this.beneficiaryOwner = beneficiaryOwner;
    }

    public String getRelationshipToPolicyHolder() {
        return relationshipToPolicyHolder;
    }

    public void setRelationshipToPolicyHolder(String relationshipToPolicyHolder) {
        this.relationshipToPolicyHolder = relationshipToPolicyHolder;
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

    public PreferredContact getPreferredContact() {
        return preferredContact;
    }

    public void setPreferredContact(PreferredContact preferredContact) {
        this.preferredContact = preferredContact;
    }
}
