/* Copyright © 2017 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package rest.service.policy;




import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;


import static help.ws.rest.RestClient.HttpMethod.GET;
import static help.ws.rest.RestClient.HttpMethod.POST;
import static help.ws.rest.RestClient.HttpMethod.PUT;
import static help.ws.rest.features.AuthRemoval.NO_DATA;

import help.TestDataProvider;
import help.common.EntityService;

import help.data.TestData;
import help.utils.ResponseContainer;
import help.ws.rest.RestClient;
import help.ws.rest.conf.client.DXPRestConfiguration;
import help.ws.rest.conf.metadata.InlineRequestContext;
import rest.service.policy.modeling.auto.*;
import rest.service.policy.modeling.quotes.PolicyResponse;

public class DXPQuickquoteAutoService implements DXPQuickquoteProductAPI, EntityService {
    public static DXPQuickquoteAutoService INSTANCE = new DXPQuickquoteAutoService();
    private RestClient client;
    private static final String MODULE_NAME = "quickquote.auto";
    private static final String ALIAS = "QUICKQUOTE.AUTO";
    private static final String CREATE_ALIAS = ALIAS + ".CREATE";
    private static final String RATE_ALIAS = ALIAS + ".RATE";
    private static final String VALIDATE_ALIAS = ALIAS + ".VALIDATE";
    private static final String LOOKUP_ALIAS = ALIAS + ".LOOKUPS";
    private static final String LOOKUP_ATTRIBUTES_ALIAS = LOOKUP_ALIAS + ".ATTRIBUTES";
    private static final String LOOKUP_SEARCH_ALIAS = LOOKUP_ALIAS + ".SEARCH";

    public DXPQuickquoteAutoService() {
        client = new RestClient(getRestModuleName(), DXPRestConfiguration.INSTANCE);
    }

    public Response createQuote(TestData testData) {
        return client.processRequest(CREATE_ALIAS, POST, new AutoQuote(testData));
    }

    public ResponseContainer<PolicyResponse> createQuote(AutoQuote autoQuote) {
        return new ResponseContainer<>(client.processRequest(CREATE_ALIAS, POST, autoQuote), PolicyResponse.class);
    }

    public Response getQuote(String policyNumber, String acceptLanguage) {
        return client.processRequest(ALIAS, GET, null, InlineRequestContext.builder()
                .pathParam("policyNumber", policyNumber).header("Accept-Language", acceptLanguage).user(NO_DATA, NO_DATA).build());
    }

    public Response getQuote(String policyNumber) {
        return getQuote(policyNumber, "zh-TW");
    }

    public Response updateQuote(String policyNumber, TestData testData) {
        return client.processRequest(ALIAS, PUT, new AutoQuote(testData),
                InlineRequestContext.builder().pathParam("policyNumber", policyNumber).user(NO_DATA, NO_DATA).build());
    }

    public Response updateQuote(String policyNumber, AutoQuote autoQuote) {
        return client.processRequest(ALIAS, PUT, autoQuote,
                InlineRequestContext.builder().pathParam("policyNumber", policyNumber).user(NO_DATA, NO_DATA).build());
    }

    public Response rateQuote(String policyNumber, String language) {
        return client.processRequest(RATE_ALIAS, POST, null,
                InlineRequestContext.builder().pathParam("policyNumber", policyNumber).header("Accept-Language", language).build());
    }

    public Response rateQuote(String policyNumber) {
        return rateQuote(policyNumber, "zh-TW");
    }

    public Response validateQuote(String quoteNumber) {
        return client.processRequest(VALIDATE_ALIAS, POST, null,
                InlineRequestContext.builder().pathParam("quoteNumber", quoteNumber).build());
    }

    /**
     * GET /quickquote-auto/v1/lookups/{lookupName}
     * @param lookupName
     * @return
     */
    public ResponseContainer<Lookup> getLookup(String lookupName){
        return getLookup(lookupName, "zh-TW");
    }

    public ResponseContainer<Lookup> getLookup(String lookupName, String language){
        Response response =  client.processRequest(LOOKUP_ALIAS, GET, null,
                InlineRequestContext.builder().pathParam("lookupName", lookupName).header("Accept-Language", language).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer(response, new GenericType<Lookup>(){});
    }

    /**
     * GET /quickquote-auto/v1/lookups/{lookupName}/attributes
     * @param lookupName
     * @return
     */
    public ResponseContainer<List<LookupAttribute>> getLookupAttributes(String lookupName){
        return getLookupAttributes(lookupName, "zh-TW");
    }

    public ResponseContainer<List<LookupAttribute>> getLookupAttributes(String lookupName, String language){
        Response response =  client.processRequest(LOOKUP_ATTRIBUTES_ALIAS, GET, null,
                InlineRequestContext.builder().pathParam("lookupName", lookupName)
                        .header("Accept-Language", language).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer(response, new GenericType<List<LookupAttribute>>(){});
    }

    /**
     * GET /quickquote-auto/v1/lookups/search
     * @param lookupName
     * @return
     */
    public ResponseContainer<List<LookupSearch>> getLookupSearch(String lookupName){
        Response response = client.processRequest(LOOKUP_SEARCH_ALIAS, GET, null,
                InlineRequestContext.builder().queryParam("names", lookupName).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer<>(response, new GenericType<List<LookupSearch>>(){});
    }

    public ResponseContainer<List<LookupSearch>> getLookupSearch(List<String> lookupNames){
        Response response = client.processRequest(LOOKUP_SEARCH_ALIAS, GET, null,
                InlineRequestContext.builder().queryParam("names", String.join("%2C",lookupNames)).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer<>(response, new GenericType<List<LookupSearch>>(){});
    }

    /**
     * GET /quickquote-auto/v1/vehicles
     * @return
     */
    public ResponseContainer<List<Vehicles>> getVehicles(Map<String, String> map){
        Response response = client.processRequest("QUICKQUOTE.AUTO.VEHICLES", GET, null,
                InlineRequestContext.builder().queryParams(map).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer<>(response, new GenericType<List<Vehicles>>(){});
    }

    /**
     * GET /quickquote-auto/v1/vehicles/{year}/makes
     * @param year
     * @return
     */
    public ResponseContainer<List<String>> getMakes(String year){
        Response response = client.processRequest("QUICKQUOTE.AUTO.MAKES", GET, null,
                InlineRequestContext.builder().pathParam("year", year).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer<>(response, new GenericType<List<String>>(){});
    }

    /**
     * GET /quickquote-auto/v1/vehicles/{year}/makes/{make}/models
     * @param year
     * @param make
     * @return
     */
    public ResponseContainer<List<String>> getModels(String year, String make){
        Response response = client.processRequest("QUICKQUOTE.AUTO.MAKES.MODELS", GET, null,
                InlineRequestContext.builder().pathParam("year", year).pathParam("make", make).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer<>(response, new GenericType<List<String>>(){});
    }

    /**
     * GET /quickquote-auto/v1/vehicles/{year}/makes/{make}/models/{model}
     * @param year
     * @param make
     * @param model
     * @return
     */
    public ResponseContainer<List<Vehicles>> getVehicles(String year, String make, String model){
        Response response = client.processRequest("QUICKQUOTE.AUTO.MAKES.MODELS.VEHICLE", GET, null,
                InlineRequestContext.builder().pathParam("year", year).pathParam("make", make).pathParam("model", model).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer<>(response, new GenericType<List<Vehicles>>(){});
    }

    /**
     * GET /quickquote-auto/v1/vehicles/{year}/makes/{make}/models/{model}/styles
     * @param year
     * @param make
     * @param model
     * @return
     */
    public ResponseContainer<List<String>> getStyles(String year, String make, String model){
        Response response = client.processRequest("QUICKQUOTE.AUTO.MAKES.MODELS.STYLES", GET, null,
                InlineRequestContext.builder().pathParam("year", year).pathParam("make", make).pathParam("model", model).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer<>(response, new GenericType<List<String>>(){});
    }

    /**
     * GET /quickquote-auto/v1/vehicles/years
     * @return
     */
    public ResponseContainer<List<String>> getAllYears(){
        Response response = client.processRequest("QUICKQUOTE.AUTO.VEHICLES.YEARS", GET, null);
        return new ResponseContainer<>(response, new GenericType<List<String>>(){});
    }

    /**
     * GET /quickquote-auto/v1/find-vehicles
     * @param map
     * @return
     */
    public ResponseContainer<List<Vehicles>> getFindVehicles(Map<String, String> map){
        Response response = client.processRequest("QUICKQUOTE.AUTO.FIND.VEHICLES", GET, null,
                InlineRequestContext.builder().queryParams(map).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer<>(response, new GenericType<List<Vehicles>>(){});
    }

    /**
     * POST /quickquote-auto/v1/quotes/{policyNumber}/versions
     * @param policyNumber
     * @param autoVersion
     * @return
     */
    public ResponseContainer<QuoteVersions> postQuoteVersions(String policyNumber, AutoVersion autoVersion){
        Response response = client.processRequest("QUICKQUOTE.AUTO.POST.VERSION", POST, autoVersion,
                InlineRequestContext.builder().pathParam("policyNumber", policyNumber).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer<>(response, new GenericType<QuoteVersions>(){});
    }

    /**
     * GET /quickquote-auto/v1/quotes/{policyNumber}/version
     * @param policyNumber
     * @param language
     * @return
     */
    public ResponseContainer<List<QuoteVersions>> getQuoteVersions(String policyNumber, String language){
        Response response = client.processRequest("QUICKQUOTE.AUTO.GET.VERSION", GET, null,
                InlineRequestContext.builder().pathParam("policyNumber", policyNumber).header("Accept-Language", language).build());
        return new ResponseContainer<>(response, new GenericType<List<QuoteVersions>>(){});
    }

    public ResponseContainer<List<QuoteVersions>> getQuoteVersions(String policyNumber){
        return  getQuoteVersions(policyNumber, "zh-TW");
    }

    /**
     * PUT /quickquote-auto/v1/quotes/{policyNumber}/active-version
     * @param policyNumber
     * @param pendingRevisionNo
     * @param revisionNo
     * @param language
     * @return
     */
    public Response putActiveVerion(String policyNumber, String pendingRevisionNo,  String revisionNo, String language){
        return client.processRequest("QUICKQUOTE.AUTO.PUT.ACTIVE.VERSION", PUT, Entity.json("{}"),
                InlineRequestContext.builder().pathParam("policyNumber", policyNumber).queryParam("pendingRevisionNo",
                        pendingRevisionNo).queryParam("revisionNo", revisionNo).header("Accept-Language", language).user(NO_DATA, NO_DATA).build());
    }

    public Response putActiveVerion(String policyNumber, String pendingRevisionNo,  String revisionNo){
        return putActiveVerion(policyNumber, pendingRevisionNo, revisionNo, "zh-TW");
    }

    public Response getQuoteCompulsoryDocuments(Map<String, String> map){
        return client.processRequest("QUICKQUOTE.AUTO.GET.COMPULSORY.DOCUMENTS",GET, null,
                InlineRequestContext.builder().queryParams(map).user(NO_DATA, NO_DATA).build());
    }


    /**
     * GET /quickquote-auto/v1/packages
     * @param typeOfPolicyCd
     * @param language
     * @return
     */
    public ResponseContainer<List<Packages>> getPackages(String typeOfPolicyCd, String language){
        Response response =  client.processRequest("QUICKQUOTE.AUTO.GET.PACKAGES", GET, null,
                InlineRequestContext.builder().queryParam("typeOfPolicyCd", typeOfPolicyCd).header("Accept-Language", language).user(NO_DATA, NO_DATA).build());
        return new ResponseContainer<>(response,  new GenericType<List<Packages>>(){});
    }

    public ResponseContainer<List<Packages>> getPackages(String typeOfPolicyCd){
        return  getPackages(typeOfPolicyCd, "zh-TW");
    }


    /**
     * POST /quickquote-auto/v1/validate/registration-number
     * @return
     */
    public Response postRegistrationNumber(RegistrationNumber registrationNumber){
        return client.processRequest("QUICKQUOTE.AUTO.VALIDATE.REGISTRATION.NUMBER", POST,registrationNumber,
                InlineRequestContext.builder().user(NO_DATA, NO_DATA).build());
    }

    /**
     * POST /quickquote-auto/v1/packages/{packageCd}/values
     * @return
     */
    public Response postPackageCdValues(String packageCd, PackageCdValues packageCdValues){
        return client.processRequest("QUICKQUOTE.AUTO.PACKAGE.PACKAGECD.VALUES", POST, packageCdValues,
                InlineRequestContext.builder().pathParam("packageCd", packageCd).user(NO_DATA,NO_DATA).build());
    }

    /**
     * GET /quickquote-auto/v1/quote-policy
     * @return
     */
    public Response getQuotePolicy(InlineRequestContext inlineRequestContext){
        return client.processRequest("QUICKQUOTE.AUTO.QUOTE.POLICY",  GET, null, inlineRequestContext);
    }


    /**
     * GET /quickquote-auto/v1/quotes/{policyNumber}/uw-rules
     * @return
     */
    public Response getPolicyUWRules(String policyNumber, String transactionEffectiveDate, String language){
        return client.processRequest("QUICKQUOTE.AUTO.UW.RULES", GET, null,
                InlineRequestContext.builder().user(NO_DATA,NO_DATA)
                        .pathParam("policyNumber", policyNumber)
                        .queryParam("transactionEffectiveDate", transactionEffectiveDate)
                        .header("Accept-Language", language).build());
    }

    public Response getPolicyUWRules(String policyNumber, String transactionEffectiveDate){
        return getPolicyUWRules(policyNumber, transactionEffectiveDate, "zh-TW");
    }
    

    protected String getRestModuleName() {
        return "DXP "+ MODULE_NAME;
    }

    @Override
    public TestData defaultTestData() {
        return TestDataProvider.getJSONTestDataProvider().get("modules/dxp/quickquote/auto");
    }
}
