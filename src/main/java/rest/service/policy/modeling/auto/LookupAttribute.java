/* Copyright © 2017 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent. */
package rest.service.policy.modeling.auto;

import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

/**
 * @author ikisly
 */
public class LookupAttribute extends DXPModel implements Adjustable<LookupAttribute> {
    private String code;
    private String defaultValue;
    private String entityType;
    private Integer orderNo;
    private String codeISO;
    private String productCd;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getCodeISO() {
        return codeISO;
    }

    public void setCodeISO(String codeISO) {
        this.codeISO = codeISO;
    }

    public void setProductCd(String productCd) {
        this.productCd = productCd;
    }

    public LookupAttribute(TestData testData) {
        super(testData);
    }

    public LookupAttribute() {
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getProductCd() {
        return productCd;
    }

    @Override
    public String toString() {
        return "LookupAttribute{" +
                "code='" + code + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", entityType='" + entityType + '\'' +
                ", orderNo=" + orderNo +
                ", codeISO='" + codeISO + '\'' +
                ", productCd='" + productCd + '\'' +
                '}';
    }
}
