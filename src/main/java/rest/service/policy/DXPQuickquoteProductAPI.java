/* Copyright © 2018 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package rest.service.policy;



import javax.ws.rs.core.Response;
import help.data.TestData;

public interface DXPQuickquoteProductAPI {
	
	public Response createQuote(TestData data);

    public Response getQuote(String policyNumber);

    public Response updateQuote(String policyNumber, TestData data);

    public Response rateQuote(String policyNumber);

}
