/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import help.utils.DateUtils;
import help.ws.rest.model.DXPModel;

/**
 * @Author: Vanwich
 * @Date: 6/8/2020
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Packages extends DXPModel {
    private String code;
    private String label;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.YYYY_MM_DD_T_HH_MM_SS_Z)
    private LocalDateTime effectiveDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.YYYY_MM_DD_T_HH_MM_SS_Z)
    private LocalDateTime requestDate;
    private Double authorityLevel;
    private List<Dimensions> dimensions;

    public Packages(){}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public Double getAuthorityLevel() {
        return authorityLevel;
    }

    public void setAuthorityLevel(Double authorityLevel) {
        this.authorityLevel = authorityLevel;
    }

    public List<Dimensions> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<Dimensions> dimensions) {
        this.dimensions = dimensions;
    }

    public static class Dimensions extends DXPModel{
        private List<String> vehicleUsageCd;
        private List<String> producerCd;
        private List<String> typeOfPolicyCd;
        private List<String> vehicleCategoryCd;

        public Dimensions(){}

        public List<String> getVehicleUsageCd() {
            return vehicleUsageCd;
        }

        public void setVehicleUsageCd(List<String> vehicleUsageCd) {
            this.vehicleUsageCd = vehicleUsageCd;
        }

        public List<String> getProducerCd() {
            return producerCd;
        }

        public void setProducerCd(List<String> producerCd) {
            this.producerCd = producerCd;
        }

        public List<String> getTypeOfPolicyCd() {
            return typeOfPolicyCd;
        }

        public void setTypeOfPolicyCd(List<String> typeOfPolicyCd) {
            this.typeOfPolicyCd = typeOfPolicyCd;
        }

        public List<String> getVehicleCategoryCd() {
            return vehicleCategoryCd;
        }

        public void setVehicleCategoryCd(List<String> vehicleCategoryCd) {
            this.vehicleCategoryCd = vehicleCategoryCd;
        }
    }
}
