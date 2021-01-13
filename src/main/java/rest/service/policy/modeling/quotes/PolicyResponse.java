/* Copyright © 2017 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package rest.service.policy.modeling.quotes;



import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import help.utils.DateUtils;
import help.ws.rest.model.DXPModel;

public class PolicyResponse extends DXPModel {
    private String policyNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.YYYY_MM_DD_T_HH_MM_SS_Z)
    private LocalDateTime transactionEffectiveDate;

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public LocalDateTime getTransactionEffectiveDate() {
        return transactionEffectiveDate;
    }

    public void setTransactionEffectiveDate(LocalDateTime transactionEffectiveDate) {
        this.transactionEffectiveDate = transactionEffectiveDate;
    }
}
