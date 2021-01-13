/*
 * Copyright © 2020 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.
 */
package rest.service.policy.modeling.auto;



import java.util.List;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

public class LookupSearch extends DXPModel implements Adjustable<LookupSearch> {

    private String name;
    private List<LookupAttribute> values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LookupAttribute> getValues() {
        return values;
    }

    public void setValues(List<LookupAttribute> values) {
        this.values = values;
    }

    public LookupSearch(TestData td){super(td);}

    public LookupSearch(){}

    @Override
    public String toString() {
        return "LookupSearch{" +
                "name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
