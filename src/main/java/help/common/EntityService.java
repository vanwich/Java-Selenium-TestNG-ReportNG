/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package help.common;

import java.util.Objects;
import help.data.TestData;
import help.exceptions.JstrException;

/**
 * Common Interface for all entities used within project. I.e. IndividualBenefitsPolicy, IBilling, IClaim etc. *
 */
public interface EntityService {

    /**
     * Provides ability to get default test data relevant to the entity.
     *
     * @param testDataKey - define keys if some particular test data is needed
     * @return - {@link TestData} all test data objects fetched from files stored in entity's test data path
     */
    default TestData getDefaultTestData(String... testDataKey) {
        if (Objects.nonNull(defaultTestData())) {
            return Objects.nonNull(testDataKey) && testDataKey.length != 0 ? defaultTestData().getTestData(testDataKey).resolveLinks() : defaultTestData().resolveLinks();
        }
        throw new JstrException(String.format("Not supported or absent for current type of entity[%1$s]. Please implement method or add test data reference to Entity Service implementation if needed.",
                this.getClass()));
    }

    /**
     * Used only for Service Impl classes, do not use in any other places
     *
     * @return {@link TestData} object relevant to particular service implementation
     */
    TestData defaultTestData();

}
