/* Copyright © 2017 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 * CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent. */
package rest.service.policy.modeling.auto;



import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

/**
 * @author ikisly
 */
public class Lookup extends DXPModel implements Adjustable<Lookup> {
    private Map<String, String> lookupValue;

    @JsonAnySetter
    public void add(String key, String value) {
        if(isNull(lookupValue)){
            lookupValue = new HashMap<>();
        }
        lookupValue.put(key, value);
    }

    public Map<String, String> getLookupValue() {
        return lookupValue;
    }

    public Lookup(TestData testData) {
        super(testData);
    }

    public Lookup() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Lookup lookup = (Lookup) o;

        return lookupValue != null ? lookupValue.equals(lookup.lookupValue) : lookup.lookupValue == null;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
