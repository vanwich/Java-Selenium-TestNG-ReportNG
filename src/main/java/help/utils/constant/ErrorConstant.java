package help.utils.constant;

public enum ErrorConstant {
    AGENT_CUSOMER_NO_EMPLOYMENTS_OTHER_EMPLOYMENTINFORID("CUSTOMER_EMPLOYMENT_NOT_FOUND", "Customer employment %s not found"),
    AGENT_CUSTOMER_CUSTOMER_ACCOUNT_NOT_FOUND("CUSTOMER_ACCOUNT_NOT_FOUND", "Customer Account for Customer with number %s not found."),
    AGENT_CUSTOMER_NOT_INDIVIDUAL_CUSTOMER_TYPE("NOT_INDIVIDUAL_CUSTOMER_TYPE", "Customer with number %s is not Individual."),
    AGENT_CUSTOMER_INTERNAL_SERVER_ERROR("500", "Internal server error."),
    AGENT_CUSTOEMR_ADDITIONAL_NAMES_EXCEED_MAX_SIZE("ADDITIONAL_NAMES_EXCEED_MAX_SIZE", "Only 10 Additional Names can be added."),
    AGENT_CUSTOEMR_NOT_BUSINESS_CUSTOMER_TYPE("NOT_BUSINESS_CUSTOMER_TYPE", "Customer with number %s is not Business."),
    AGENT_CUSTOEMR_EXIST_CUSTOEMR_EXIST_TARGETNUMBER_DIFFERENT_TYPE("422", "Source and target customers should be Individual or Non-Individual at the same time."),
    AGENT_CUSTOEMR_CUSTOMER_NOT_FOUND("CUSTOMER_NOT_FOUND", "Customer with number %s not found."),
    AGENT_CUSTOEMR_ADDITIONAL_NAME_NOT_FOUND("ADDITIONAL_NAME_NOT_FOUND", "Additional name %s not found"),
    AGENT_CUSTOMER_ERROR_DXP_GATEWAY_INTERNAL_ERROR("ERROR_DXP_GATEWAY_INTERNAL_ERROR", "Unable to deserialize data."),
    AGENT_CUSTOEMR_LEGAL_REPRESENTATIVE("422", "'Legal Representative' is required"),
    AGENT_CUSTOEMR_EIN("422", "'EIN' must be 8 digits"),
    AGENT_CUSTOMER_BUSINESS_LEAD("422", "Invalid customerType value: BUSINESS_LEAD. Only following types are allowed: [INDIVIDUAL_LEAD]"),
    AGENT_CUSTOEMR_ERROR_SERVICE_BAD_REQUEST("ERROR_SERVICE_BAD_REQUEST", "Text '%s' could not be parsed at index 4"),
    AGENT_DOCUMENTS_EFOLDER002("EFOLDER002", "Entity %s with id %s does not exist."),
    AGENT_DOCUMENTS_EFOLDER023("EFOLDER023", "Entity information or document ID is required."),
    AGENT_DOCUMENTS_EFOLDER027("EFOLDER027", "Same number of entity types and entity reference numbers must be passed."),
    AGENT_TASK_TNAEC_001("TNAEC_001", "User Task with id : %s not found in within available active Tasks list. "),
    AGENT_TASK_TNAEC_422("422", "Assignment value is invalid"),
    AGENT_TASK_TNAEC_UAEC001("UAEC001", "User is unavailable."),
    AGENT_TASK_422("422", "The request was well-formed but was unable to be followed due to semantic errors (invalid field value, validation rules and etc)"),
    AGENT_TASK_TEF001("TEF001", "Unable to attach document to inactive Task with id %s. (Task status is COMPLETED)."),
    AGENT_TASK_TDNEC001("TDNEC001", "Task does not exist."),
    AGENT_TASK_TASK_DOES_NOT_EXIST("422", "Task does not exist."),
    AGENT_TASK_USER_422("422", "Please refine search criteria"),
    AGENT_TASK_WONPEC001("WONPEC001", "Priority is not set or is not valid for this task with name Opportunity is in cold and id %s."),
    AGENT_CLAIMS_VALIDATION_ERRORS("Validation errors", "The request was well-formed but was unable to be followed due to semantic errors (invalid field value, validation rules and etc)"),

    AGENT_POLICY_WS043_ERROR("WS043", "Policy #%s and transaction effective date 3/23/20 12:00 AM not found."),
    AGENT_POLICY_WS043("WS043", "Policy #%s and transaction effective date %s 12:00 AM not found."),

    AGENT_DOCUMENTS_EFOLDER005("EFOLDER005", "Document with id %s does not exist."),

    CUSTOMER_POLICIES_ERROR_SERVICE_VALIDATION("ERROR_SERVICE_VALIDATION", "Unable to find policy details."),
    CUSTOMER_CLAIMS_DOCUMENTS_ERROR_SERVICE_VALIDATION("ERROR_SERVICE_VALIDATION", "Unable to find claim details."),
    CUSTOMER_CLAIMS_FOLDER_NOT_FOUND("ERROR_SERVICE_OBJECT_NOT_FOUND", "Folder not found."),
    CUSTOMER_CLAIMS_CLAIM_INFORMATION_IS_NOT_AVAILABLE("ERROR_SERVICE_VALIDATION", "Claim information is not available."),

    QUICKQUOTE_TASK_ERROR_SERVICE_BAD_REQUEST_UNABLE_TO_DESERIALIZE_DATA("ERROR_SERVICE_BAD_REQUEST", "Unable to deserialize data."),
    CUSTOMER_RENEWALS_ERROR_SERVICE_OBJECT_NOT_FOUND("ERROR_SERVICE_OBJECT_NOT_FOUND", "Policy information is not available."),
    CUSTOMER_RENEWALS_POLICY_NUMBER_NOT_THE_SAME("POLICY_NUMBER_NOT_THE_SAME", "Policy number %1$S and Policy number in request body %2$S should be the same."),
    CUSTOMER_CLAIMS_ERROR_SERVICE_VALIDATION("ERROR_SERVICE_VALIDATION", "Unable to retrieve customer claim details."),


    QUICKQUOTE_AUTO_ERROR_SERVICE_OBJECT_NOT_FOUND("ERROR_SERVICE_OBJECT_NOT_FOUND", "Object not found."),
    QUICKQUOTE_LEAD_ERROR_SERVICE_BAD_REQUEST_UNABLE_TO_DESERIALIZE_DATA("ERROR_SERVICE_BAD_REQUEST", "Unable to deserialize data."),
    QUICKQUOTE_LEAD_ERROR_SERVICE_BAD_REQUEST("ERROR_SERVICE_BAD_REQUEST", "Text '%s' could not be parsed at index 4"),
    QUICKQUOTE_LEAD_ERROR_SERVICE_VALIDATION("ERROR_SERVICE_VALIDATION", "Input parameter is invalid, please input first name, last name and data of birth or tax id."),
    QUICKQUOTE_LEAD_CUSTOMER_NOT_FOUND("CUSTOMER_NOT_FOUND", "Customer with number %s not found."),
    QUICKQUOTE_LEAD_422("422", "Look-up %s is not supported"),

    QUICKQUOTE_AUTO_QUOTES_PFW016("PFW016", "Entity with a number %s was not found"),
    QUICKQUOTE_AUTO_QUOTES_PFW016_TW("PFW016", "找不到編號為%s的實體"),
    QUICKQUOTE_AUTO_WS056_EU("HOT_WS056", "Quote %1$s with specified revision number %2$s and pending revision number %3$s not found."),
    QUICKQUOTE_AUTO_WS056_TW("HOT_WS056", "找不到指定的報價單%1$s，對應的簽發後版本號為%2$s，簽發時的版本號為%3$s。"),
    QUICKQUOTE_AUTO_WS057("HOT_WS057", "車種 %s 無效的。"),
    QUICKQUOTE_AUTO_422("422", "Validation errors"),
    QUICKQUOTE_AUTO_422_VEHICLECATEGORYCD("422", "Look-up vehicleCategoryCd is not supported"),
    QUICKQUOTE_AUTO_ERROR_SERVICE_BAD_REQUEST("Validation errors", "producerCd or subProducerCd is requird"),
    QUICKQUOTE_AUTO_VALIDATION_ERRORS("Validation errors", "The request was well-formed but was unable to be followed due to semantic errors (invalid field value, validation rules and etc)"),
    QUICKQUOTE_AUTO_PACKAGE("ERROR_SERVICE_VALIDATION", "The package code(%s) is not defined"),
    QUICKQUOTE_AUTO_TRANSACTION_EFFECTIVE_DATE_FORMAT_ERROR("TRANSACTION_EFFECTIVE_DATE_FORMAT_ERROR", "The format of transactionEffectiveDate should be YYYY-MM-DD."),
    QUICKQUOTE_AUTO_POLICY_ERROR("Policy error", "Customer find with ${%1$s},Quote create with ${%2$s},The premium ${totalPremium:%3$s} of quote is not the same with the premium ${subsystemPremium:%4$s} of ${B2C}"),

    QUICKQUOTE_SECURITY_VALIDATION_ERRORS("Validation errors", "Please input 64 charactors as security key."),

    QUICKQUOTE_POLICY_VALIDATION_ERRORS_EXPIRATIONDATEFROM("Validation errors", "expirationDateFrom can not be empty"),
    QUICKQUOTE_POLICY_VALIDATION_ERRORS_EXPIRATIONDATETILL("Validation errors", "expirationDateTill can not be empty"),
    QUICKQUOTE_POLICY_ERROR_SERVICE_BAD_REQUEST("ERROR_SERVICE_BAD_REQUEST", "Text '20200808' could not be parsed at index 0"),
    QUICKQUOTE_POLICY_VALIDATION_ERRORS("Validation errors", "please make sure that time expirationDateTill is after time expirationDateFrom"),

    //Base
    USER_DOESNT_HAVE_ACCESS_TO_THE_RESOURCE("403", "Authentication succeeded but authenticated user doesn't have access to the resource."),
    NOT_FOUND("404", "Not Found"),
    REQUEST_WAS_WELL_FORMED_DUE_TO_SEMANTIC_ERRORS("422", "The request was well-formed but was unable to be followed due to semantic errors (invalid field value, validation rules and etc)"),
    INVALID_ENTITY_VERSION_FIELD_VALUE("422", "Invalid entity version field value"),
    PAYMENT_ACTION_IS_NOT_ALLOWED("422", "Payment action is not allowed"),
    ELEMENT_IS_REQUIRED("422", "%s is required."),
    THE_ELEMENT_IS_REQUIRED("422", "The %s is required"),
    ELEMENT_IS_INVALID("422", "%s value is invalid"),
    SUSPENSE_END_DATE_SHOULD_BE_AFTER_TODAY("422", "Suspense end date should be after today"),

    ACCOUNT_HAS_PENDING_REFUND("ACCOUNT_HAS_PENDING_REFUND", "Unable to refund. Account #%s has pending refund"),
    ADJUSTMENT_ACTION_DOES_NOT_EXIST("ADJUSTMENT_ACTION_DOES_NOT_EXIST", "Specified adjustment actionCd does not exist"),
    AGENCY_PAYMENT_WRONG_STATUS("AGENCY_PAYMENT_WRONG_STATUS", "Service is applicable only for Agency Payment with status incomplete"),

    BATCH_PAYMENT_WRONG_STATUS("BATCH_PAYMENT_WRONG_STATUS", "Service is applicable only for Batch Payment with status %s"),
    BULK_PAYMENT_WRONG_STATUS("BULK_PAYMENT_WRONG_STATUS", "Service is applicable only for Bulk Payment with status draft"),
    SUSPENSE_WRONG_STATUS("SUSPENSE_WRONG_STATUS", "Service is applicable only for Suspense with status incomplete"),

    BALANCE_AMOUNT_CODE_FIELD_CAN_NOT_BE_BLANK("BALANCE_AMOUNT_CODE_FIELD_CAN_NOT_BE_BLANK", "Balance Amount field amount can not be blank for BILLING_FEE"),
    BILLING_ACCOUNT_NOT_FOUND("BILLING_ACCOUNT_NOT_FOUND", "Billing account #%s is not found"),
    BILLING_ACCOUNT_NOT_FOUND_FOR_POLICY("BILLING_ACCOUNT_NOT_FOUND_FOR_POLICY", "Billing account is not found for policy #%s with effective date: .*"),

    COMMUNICATION_WITH_NUMBER_NOT_FOUND("COMMUNICATION_NOT_FOUND", "Communication with number %s not found."),
    AGENT_CUSTOMER_NOT_FOUND("CUSTOMER_NOT_FOUND", "Customer #%s not found"),

    CLEAR_NOT_AVAILABLE("CLEAR_NOT_AVAILABLE", "Clear action is not available"),
    CREDIT_CARD_INVALID_CARD_NUMBER("CREDIT_CARD_INVALID_CARD_NUMBER", "Invalid credit card number for TEST type"),

    DECLINE_NOT_AVAILABLE("DECLINE_NOT_AVAILABLE", "Decline is not available"),

    ENTITY_CUSTOMER_WITH_ID_DOES_NOT_EXIST("EFOLDER002", "Entity Customer with id %s does not exist."),
    ERROR_ENTITY_NOT_EXISTS_MSG("EFOLDER002", "error_entity_not_exists_msg"),
    FOLDER_WITH_ID_DOES_NOT_EXIST("EFOLDER004", "Folder with id %s does not exist."),
    DOCUMENT_WITH_NAME_ALREADY_EXISTS("EFOLDER007", "Document with name %s already exists in %s folder."),
    DOCUMENT_NAME_IS_REQUIRED("EFOLDER020", "Document name is required."),
    FILE_URL_IS_REQUIRED("EFOLDER025", "File URL is required."),

    TEXT_COULD_NOT_BE_PARSED("ERROR_SERVICE_BAD_REQUEST", "Text .* could not be parsed: .*"),
    GENESIS_ENTITY_TYPE_WITH_MODEL_NAME_NOT_FOUND("ERROR_SERVICE_BAD_REQUEST", "Genesis entity type with model name: %s not found."),
    GENESIS_MODEL_WITH_NAME_IS_NOT_REGISTERED("ERROR_SERVICE_BAD_REQUEST", "Genesis model with name '%s' is not registered."),
    MISSING_TYPE_ID("ERROR_SERVICE_BAD_REQUEST", "Unable to deserialize data. Missing type id when trying to resolve subtype of [simple type, class agent.services.policy.dto.auto.GenesisAutoPolicy]: missing type id property '%s'"),
    MISSING_TYPE_ID_PROPERTY("ERROR_SERVICE_BAD_REQUEST", "Unable to serialize data. Missing type id when trying to resolve subtype of [simple type, class agent.services.policy.dto.auto.GenesisAutoPolicy]: missing type id property '%s'"),
    UNABLE_TO_DESERIALIZE_DATA("ERROR_SERVICE_BAD_REQUEST", "Unable to deserialize data."),
    UNABLE_TO_DESERIALIZE_DATA_DATE("ERROR_SERVICE_BAD_REQUEST", "Unable to deserialize data. Text '%s' could not be parsed at index 4"),
    MISSING_PARAMETER("ERROR_SERVICE_BAD_REQUEST", "Missing parameter: %s"),

    COMMUNICATION_NOT_FOUND("ERROR_SERVICE_OBJECT_NOT_FOUND", "Communication not found."),
    DXP_QUOTE_NOT_FOUND("ERROR_SERVICE_OBJECT_NOT_FOUND", "Quote not found."),
    DOCUMENT_NOT_FOUND("ERROR_SERVICE_OBJECT_NOT_FOUND", "Document not found."),
    CUSTOMER_WITH_NUMBER_NOT_FOUND("ERROR_SERVICE_OBJECT_NOT_FOUND", "Customer with number: %s not found."),
    OBJECT_NOT_FOUND("ERROR_SERVICE_OBJECT_NOT_FOUND", "Object not found."),

    POLICY_INFORMATION_IS_NOT_AVAILABLE("ERROR_SERVICE_VALIDATION", "Policy information is not available."),
    CUSTOMER_ID_AND_TYPE_SHOULD_BE_BOTH_SPECIFIED("ERROR_SERVICE_VALIDATION", "Customer id and type should be both specified."),
    CUSTOMER_TYPE_CAN_NOT_BE_NULL_OR_EMPTY("ERROR_SERVICE_VALIDATION", "Customer type can not be 'null' or empty."),
    CUSTOMER_SHOULD_NOT_BE_EMPTY("ERROR_SERVICE_VALIDATION", "Customer should not be empty"),
    WRONG_DECLINE_REASON("ERROR_SERVICE_VALIDATION", "Wrong decline reason for %s decline type."),
    INVALID_TO_DECLINE_QUOTE_REQUEST("ERROR_SERVICE_VALIDATION", "Invalid to decline quote request."),
    UNABLE_TO_RETRIEVE_ACCOUNT_DETAILS("ERROR_SERVICE_VALIDATION", "Unable to retrieve account details"),
    UNABLE_TO_SAVE_PAYMENT_METHOD_DETAILS("ERROR_SERVICE_VALIDATION", "Unable to save payment method details."),
    UNABLE_TO_RETRIEVE_INFORMATION_FOR_GIVEN_VENDOR_TYPE("ERROR_SERVICE_VALIDATION", "Unable to retrieve information for given vendor type."),

    PLEASE_ENSURE_THAT_THIS_ENTITY_TYPE_EXISTS_IN_THE_SYSTEM("Entity was not found", "Please ensure that this entity type: %s with reference id: %s exists in the system"),
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", "Entity with provided information does not exists"),

    AUTHORITY_LEVEL_TYPE_DOES_NOT_EXIST("err_wrong_type", "Authority level type '%s' does not exist."),

    HOLD_ID_NOT_FOUND("HOLD_ID_NOT_FOUND", "Hold with id - #%s not found"),

    DATE_MUST_BE_BETWEEN_QUOTE_EFFECTIVE_AND_EXPIRATION_DATES("INVALID_FIELD", "Date must be between quote effective and expiration dates.*"),
    PARAMETER_PAYMENT_METHOD_ID_MUST_NOT_BE_SPECIFIED("INVALID_FIELD", "Parameter paymentMethodId must not be specified if useRecurringMethodForRefundInd is true"),
    DETAILS_ARE_REQUIRED("INVALID_FIELD", "%s Details are required"),
    FIELD_IS_REQUIRED("INVALID_FIELD", "%s is required"),
    FIELD_CAN_NOT_BE_NULL("INVALID_FIELD", "Field can not be null"),
    ALL_SPECIFIED_POLICIES_MUST_HAVE_SAME_BILLING_ACCOUNT("INVALID_FIELD", "All specified policies must have same billing account"),
    THERE_ARE_NO_POSSIBLE_DESTINATION_ACCOUNTS("INVALID_FIELD", "There are no possible destination accounts"),
    PERSONAL_BILLING_ACCOUNT_IS_NOT_FOUND("INVALID_FIELD", "Personal billing account #%s is not found"),
    BILLING_ACCOUNT_INFORMATION_MUST_BE_SPECIFIED("INVALID_FIELD", "Billing account information must be specified"),
    PARAMETER_PAYMENT_PLAN_CD_MUST_BE_SPECIFIED("INVALID_FIELD", "Parameter paymentPlanCd must be specified"),
    INVALID_FIELD_SPECIFIED_POLICY_NOT_FOUND("INVALID_FIELD", "Policy with number - #%s and effective date - .* not found"),
    SPECIFIED_DESTINATION_ACCOUNT_IS_NOT_AVAILABLE("INVALID_FIELD", "Specified destination account - %s is not available"),
    PLEASE_REMOVE_BILLABLE_POLICY_DUPLICATES_FROM_REQUEST("INVALID_FIELD", "Please remove billable policy - .* duplicates from request"),

    INVOICE_CANNOT_BE_DISCARDED("INVOICE_CANNOT_BE_DISCARDED", "Invoice cannot be discarded"),

    PAYMENT_PLAN_NOT_FOUND("PAYMENT_PLAN_NOT_FOUND", "Unable to find payment plan with code %s"),
    POLICY_NOT_FOUND("POLICY_NOT_FOUND", "Policy #.* not found"),
    POLICY_NOT_FOUND_NO_ACCOUNT("POLICY_NOT_FOUND_NO_ACCOUNT", "Policy #.* not found"),

    ENTITY_OF_TYPE_AND_WITH_A_NUMBER_WAS_NOT_FOUND("PFW016", "Entity with a number %s was not found"),

    SAME_PAYMENT_PLAN("SAME_PAYMENT_PLAN", "Nothing to change, Quarterly plan already used for policy"),

    SUSPENSE_NOT_FOUND_FOR_ID("SUSPENSE_BY_ID_NOT_FOUND", "Suspense not found for id %s"),
    SUSPENSE_TOTAL_AMOUNT_DOES_NOT_MATCH_ALLOCATIONS_AMOUNT("SUSPENSE_NOT_ALLOCATED", "Suspense total amount does not match allocations amount"),
    SUSPENSE_REFUND_NOT_ACTIVE("SUSPENSE_REFUND_NOT_ACTIVE", "Transaction with refundId - %s is not an active suspense refund transaction"),
    SERVICE_IS_APPLICABLE_ONLY_FOR_SUSPENSE_WITH_STATUS_INCOMPLETE("SUSPENSE_WRONG_STATUS", "Service is applicable only for Suspense with status incomplete"),

    SPECIFIED_POLICY_NOT_FOUND("SPECIFIED_POLICY_NOT_FOUND", "Policy with number - #%s and effective date - .* not found"),
    SPECIFIED_SUBTYPE_CODE_NOT_APPLICABLE("SPECIFIED_SUBTYPE_CODE_NOT_APPLICABLE", "Specified other transaction subTypeCd - %s not applicable for type -%s"),

    TRANSACTION_IS_REVERSED("TRANSACTION_IS_REVERSED", "Specified transaction is reversed"),
    TRANSACTION_NOT_FOUND("TRANSACTION_NOT_FOUND", "Transaction #%s not found"),
    TRANSACTION_NOT_PAYMENT("TRANSACTION_NOT_PAYMENT", "Transaction #%s is not Payment"),

    BUNDLE_STATUS_IS_NOT_VALID("WRONG_BUNDLE_STATUS", "Bundle status is not valid"),
    SUBMISSION_RECEIVED_VIA_VALUE_IS_NOT_IN_RANGE("WRONG_RECEIVED_VIA_VALUE", "Submission Received Via value is not in range"),

    WRONG_TRANSACTION_TYPE_CD("WS046", "Wrong transactionTypeCd: quote."),
    BUNDLE_NOT_FOUND("WS047", "Bundle %s not found."),
    CUSTOMER_NOT_FOUND("WS048", "Customer %s not found."),
    QUOTE_NOT_FOUND_TW("WS050", "找不到報價 %s。"),
    QUOTE_NOT_FOUND("WS050", "Quote %s not found."),
    BUNDLE_NOT_EMPTY("WS051", "Bundle %s not empty."),
    BUNDLE_CAN_NOT_BE_PROPOSED("WS052", "Bundle %s can not be proposed because Propose action is available only for bundles with quotes in Premium Calculated, Proposed and Bound statuses."),

    BILLING_ACCOUNT_PRODUCT_MISSING_TMPL("bch0001", "Billing account product is missing for policy %s"),
    CLIENT_REQUEST_HAS_FAILED("bgw0001", "Client request has failed with a message of: .*"),
    POLICY_ENTITY_UPDATE_IN_PROGRESS("bgw0001", "Client request has failed with a message of: Policy/Quote entity update in progress."),
    URI_CANNOT_BE_FOUND("bgw0002", "URI: .* cannot be found"),
    FAILED_TO_PARSE_MESSAGE_BODY_AS_JSON("bgw0003", "Failed to parse message body as JSON"),
    NO_OR_INVALID_AUTHENTICATION_DETAILS_ARE_PROVIDED("bgw0006", "No or invalid authentication details are provided"),
    INVALID_ENDPOINT_INPUT("bgw0007", "Invalid endpoint input"),
    USER_NOTE_DOES_NOT_EXIST("bm0010", "User Note does not exist"),
    UPDATE_OF_TASK_HAS_FAILED("bpmg0001", "Update of task %s has failed"),
    NEW_DESCRIPTION_FOR_TASK_EXCEEDS_4000_CHARACTER_LIMIT("bpmg0003", "New description for task %s exceeds 4000 character limit"),
    NEW_DUE_DATE_FOR_TASK_IS_IN_THE_PAST("bpmg0004", "New due date for task %s is in the past"),
    CANNOT_FIND_TASK_WITH_ID("bpmg0005", "Task %1$s could not be completed, due to: Cannot find task with id %1$s"),

    BRAND_VALIDATION_FAILURE("brandf0001", "Brand validation failure"),
    BRAND_EXPIRATION_DATE_VALIDATION_ERROR("brandf0005", "Brand effective date is after expiration"),
    BRAND_EFFECTIVE_DATE_VALIDATION_ERROR("brandf0006", "Brand effective date can not be earlier than brand creation date"),

    COMMAND_EXECUTION_FAILED_UNABLE_TO_FIND_FACTORY_FOR("ch0002", "Command execution failed: No billing found for name %s"),
    ROOT_REVISION_NUMBER_IS_MISSING("ch0002", "Command execution failed: Root revision number is missing"),
    KEY_SHOULD_NOT_BE_NULL("ch0002", "Command execution failed: key should not be null!"),
    ATTRIBUTE_IS_COMPLEX_TYPE("ch0002", "Command execution failed: Attribute is .* is complex type, primitive is expected"),
    LOOKUP_EXPIRATION_DATE_IS_BEFORE_EFFECTIVE("cle0002", "Expiration date is before effective"),
    LOOKUP_OVERLAPPING_DATES_BETWEEN_LOOKUPS("cle0003", "Overlapping dates between lookups.*"),
    OBJECT_CANNOT_BE_PRESENT_AS_CUSTOMER("cli0002", "Object cannot be presented as customer."),
    LOOKUP_VALIDATION_FAILED_FOR_CREATE_LOOKUP_TASK("clr0001", "Validation failed for create lookup task"),
    FILTER_FIELD_DOES_NOT_EXIST_TMPL("cols0001", "Field with name '%s' is not found on entity '.*'"),

    VALUE_CANNOT_BE_EMPTY_ON_ATTRIBUTE("comp0002", "Value cannot be empty on attribute: %s"),
    DIFFERENT_TYPES_COULD_NOT_BE_COMPARED("comp0003", "Entities from different types could not be compared"),
    REVISIONS_OUT_OF_SEQUENCE("comp0003", "There are revisions that are out of sequence , all revisions: .*"),
    CONFLICTING_CHANGES_ERROR_MESSAGE("comp0005", "Conflicting changes"),
    REJECT_AUTO_ROLLON_ERROR_MESSAGE("comp0007", "The number of provided resolutions is exceeding the number of existing conflicts"),

    BUSINESS_VALIDATION_IS_FAILED("crmv0001", "Business validation is failed"),
    AT_LEAST_ONE_ADDRESS_IS_REQUIRED("crmv0002", "At least one Address is required"),
    AT_LEAST_ONE_CONTACT_IS_REQUIRED("crmv0002", "At least one Contact is required"),
    AT_LEAST_ONE_LEGAL_ADDRESS_IS_REQUIRED("crmv0002", "At least one Legal Address is required"),
    DATE_CANNOT_BE_IN_FUTURE("LegalEntityDateStartedCannotBeInTheFuture", "Date cannot be in the future"),
    DATE_OF_BIRTH_CANNOT_BE_AFTER_CURRENT_DATE("crmv0002", "Date of Birth cannot be after the current date, and cannot be more than 120 years before the current date"),
    EFFECTIVE_TO_IS_LESS_THAN_EFFECTIVE_FROM("SchedulingContactInfoEffectiveDatesComparison", "Effective To is less than Effective From"),
    SIC_NAICS_REQUIRED("CrmBusinessDetailsCodeSectionsAreMandatory", "Either SIC Classification section or NAICS Classification section is required"),
    EXPIRATION_DATE_IS_LESS_THAN_EFFECTIVE_DATE("GroupInfoDatesComparison", "Expiration Date is less than Effective Date"),
    INCORRECT_LEGALID_FORMAT("LegalEntityBaseLegalIdFormat.", "Incorrect legalId format. 9 digits should be entered: %s"),
    OBJECT_IS_EMPTY("crmv0002", "Object is empty"),
    RELATIONSHIP_TYPE_CANNOT_BE_EMPTY("CustomerRelationshipRelationshipTypeIsMandatory", "Relationship type cannot be empty if relationship link is specified"),
    RELATIONSHIP_TYPE_HAS_NO_APPLICABLE_VALUE("crmv0002", "Relationship type has not applicable value"),
    INCORRECT_CUSTOMER_NUMBER_FORMAT("crmv0002", "Incorrect customer number format"),
    INCORRECT_ASSOCIATIONS("crmv0004", "Incorrect associations"),
    CRM_ENTITY_DOES_NOT_EXIST("crmv0005", "Entity does not exist. Link .*"),
    OPPORTUNITY_VALIDATION_FAILED("crmv0006", "Opportunity validation is failed"),
    UNSUPPORTED_PRODUCT("crmv0007", "Unsupported product: .*"),
    ENTITY_CANNOT_BE_ASSOCIATED_WITH_ITSELF("crmv0008", "Entity cannot be associated with itself"),
    ASSOCIATED_TWICE("crmv0009", "Object cannot be associated with the same entity twice"),
    INCORRECT_STATE_FOR_MOVE_ENTITY("crmv0010", "Lead/Customer cannot be moved from %s state to %s state"),
    INCORRECT_STATE_FOR_ENTITY("crmv0011", "Incorrect state for entity"),
    LINK_VALUE_SHOULD_BE_UNIQUE("crmv0015", "Link value should be unique"),
    RETRO_CHANGES_ARE_NOT_SUPPORT("crmv0016", "Retro changes are not supported at this time"),
    SCHEDULED_UPDATE_ALREADY_EXISTS("crmv0017", "Scheduled update already exists"),
    SCHEDULED_UPDATE_DOES_NOT_EXIST("crmv0020", "Scheduled Update does not exist"),
    UNSUPPORTED_CUSTOMER_TYPE("crmv0022", "Unsupported customer type:.*"),
    UNSUPPORTED_TYPE_TARGET_CHARACTERISTIC("crmv0023", "Unsupported type for target characteristic: .*"),
    UPDATE_ENTITY_WITH_THIS_STATE_IS_FORBIDDEN("crmv0024", "Update entity with state %s is forbidden"),

    INVALID_OBJECT_MONEY("cv0003", "Invalid object json type JsonObject for attribute money"),
    INVALID_PRIMITIVE_MONEY("cv0003", "Invalid object json type JsonPrimitive for attribute money"),
    INVALID_COMMAND_PARAMETERS("cv0005", "Invalid command parameters"),
    KEY_IS_NOT_AN_OBJECT("cv0101", "Key is not an object"),
    MISSING_KEY_ATTRIBUTE("cv0102", "Missing key attribute"),
    INVALID_UUID("cv0104", "Invalid UUID string: .*"),
    MISSING_TYPE_ATTRIBUTE("cv0301", "Missing _type attribute"),
    EXPECTED_ONE_OF_ENTITY_TYPES_TMPL("cv0302", "Expected one of [%s] entity types, got SMTH"),
    URI_HAVE_BAD_FORMAT("cv0306", "One or more of URIs have bad format"),
    MODEL_NAME_DOES_NOT_MATCH_ROOT_ENTITY_TMPL("cv0403", "Entity billing name %s does not match root entity billing SMTH"),
    MISSING_MODEL_VERSION("cv0404", "Missing billing version"),
    MISSING_MODEL_NAME("cv0405", "Missing billing name"),

    USER_HAS_NO_ACCESS_TO_WRITING_ENTITY("dimfilt0003", "User has no access to writing entity, access to one or more dimensions is denied"),
    ENTITY_DIMENSION_CODE_IS_NOT_IN_THE_SCOPE("dimfv0001", "Entity dimension, code:.*"),

    DECISION_TABLE_NOT_EXIST("dtr0002", "Decision Table with table name '%s' is not part of Decision Model: %s v%s"),
    TABLE_DOES_NOT_HAVE_REQUESTED_CATEGORY("dtcd0008", "Table 'Underwriting warnings rules' does not have requested category: null"),
    TABLE_IS_ASSIGNED_TO_CATEGORY_BUT_IT_IS_NOT_PROVIDED_IN_REQUEST("dtcd0009", "Table 'Underwriting warnings rules' is assigned to category but it is not provided in request"),

    ENTITY_DOES_NOT_EXIST_OR_IS_NOT_ACCESSIBLE("entity_not_found", "Entity does not exist or is not accessible"),
    DATE_CONVERTATION_FAILURE("fc00002", "Failed to convert path param '%s' to required type LocalDate"),
    MODEL_OF_TYPE_NOT_FOUND_FOR_MODEL_NAME("ffcd0001", "Model of type %s not found for model name %s, modelVersion 1."),
    MODEL_CLASS_OF_THE_NAME_WAS_NOT_RECOGNIZED("ffcd0002", "Model class of the name %s was not recognized"),
    UNABLE_TO_CONVERT_TO_FACTORY_LINK("jwr0002", "Unable to convert value '%s' to required type FactoryLink"),
    ENTITY_IS_ALREADY_LOCKED("lck0001", "Entity .* is already locked"),
    LOCK_NOT_FOUND_FOR_ENTITY("lck0004", "Lock not found for entity .*"),
    LOCK_IS_NOT_OWNED_BY_CURRENT_USER("lck0005", "Lock of entity .* is not owned by current user"),
    INVALID_LINE_OF_BUSINESS_LOOKUP_CODES("lcv0001", "LineOfBusiness lookup code must be one of these values: \\[([^,]+,)+[^,]+\\]"),
    NO_RECORDS_FOUND_FOR_LOOKUP("lr0001", "No records found for lookup name '%s'"),

    MISSING_REQUIRED_ATTRIBUTE("mcv0101", "Missing required attribute"),
    ATTRIBUTE_VALUE_LONGER_THAN("mcv0102", "Attribute value longer than %s symbols"),
    ATTRIBUTE_HAS_LESS_INSTANCES("mcv0108", "Attribute has %s instances, min allowed is %s"),
    READ_ONLY_MISMATCH("mcv0111", "Attribute is read-only and can not be modified"),

    MORATORIUM_IS_NOT_FOUND("MORATORIUM_IS_NOT_FOUND", "Moratorium with id - #%s is not found"),

    ORG_ENTITY_DOES_NOT_EXIST("orgbr0003", "Entity does not exist. Link .*"),
    ORGBR_EFFECTIVE_DATE_CAN_NOT_BE_AFTER_EXPIRATION("orgbr0005", "Effective date can not be after expiration"),
    ORGANIZATIONS_IN_RELATIONSHIP_MUST_HAVE_DIFFERENT_ROLE_TYPES("orgbr0006", "Same organizations in relationship must have different role types"),
    ORGANIZATIONS_IN_RELATIONSHIP_MUST_HAVE_ROLE_TYPE_AS_IN_RELATIONSHIP_ORGANIZATION("orgbr0007", "Organizations in relationship must have role type as in relationship. Organization: %s, RoleType: %s"),
    ORGANIZATION_ROLE_MUST_BE_EFFECTIVE_AT_LEAST_FOR_SOME_PERIOD_OF_AGREEMENT("orgbr0008", "Organization role must be effective at least for some period of agreement. Organization: %s"),
    EFFECTIVE_DATE_SHOULD_BE_LESS_THAN_EXPIRATION("orgch0003", "Effective date should be less than expiration for the role: %s"),
    INCORRECT_DATE_OF_BIRTH_VALUE("orgv0002", "Date of birth can not be after the current date and can not be more than 120 years before the current date"),
    ORGV_EFFECTIVE_DATE_CAN_NOT_BE_AFTER_EXPIRATION("orgv0003", "Effective date can not be after expiration"),
    URI_IS_NOT_RESOLVABLE_OR_TARGET_ENTITY_DOES_NOT_EXISTS("orgv0004", "Passed URI is not resolvable or target entity does not exists"),

    PARAMETER_AMOUNT_VALUE_NOT_SPECIFIED("PARAMETER_AMOUNT_VALUE_NOT_SPECIFIED", "Parameter amount value is not specified"),
    PAYMENT_METHOD_NOT_EXIST("PAYMENT_METHOD_NOT_EXIST", "PaymentMethod with id #%s does not exist"),
    POLICY_TERM_NOT_FOUND("POLICY_TERM_NOT_FOUND", "Policy not found for number %s, effective .*"),

    PAGE_SIZE_PROVIDED_IS_HIGHER_THAN_HARD_PAGE_LIMIT("pce0001", "Page size provided is higher than hard page limit set by PagingConfig"),
    BUSINESS_CONSTRAINTS_ARE_VIOLATED("pch0001", "Operation could not be performed as business constraints are violated"),
    CANNOT_PERFORM_STATE_TRANSITION_FOR_COMMAND_TMPL("pch0004", "Cannot transit entity from state [%s] using command [%s]. Please check product state and transition configuration"),
    CANNOT_PERFORM_RENEW_POLICY_MARKED_AS_DNR("plc0004", "Cannot perform renew action - policy is marked as do not renew"),
    DATE_IS_OUTSIDE_OF_THE_QUOTE_TERM("plc0005", "Date is outside of the quote term"),
    CANNOT_PERFORM_DELETE_ACTION_ON_A_NEW_BUSINESS_QUOTE("plc0007", "Can not perform delete action on a new business quote"),
    CANNOT_PERFORM_CANCELNOTICE_OPERATION_SET_TO_TRUE("plc0008", "Cannot perform operation: PolicyDetail.cancelNotice has already set to true"),
    CANNOT_PERFORM_CANCELNOTICE_OPERATION_SET_TO_FALSE("plc0009", "Cannot perform operation: PolicyDetail.cancelNotice has already set to false"),
    CANNOT_PERFORM_REWRITE_ON_NOT_FLAT_CANCELLATION("plc0010", "Cannot perform state transition for command rewrite on not flat cancellation policy"),
    CANNOT_PERFORM_OPERATION_AS_THERE_IS_ONLY_ONE_QUOTE_VERSION("plc0011", "Cannot perform operation as there is only one quote version"),
    REQUESTED_VERSION_IS_ALREADY_SET_AS_CURRENT_QUOTE_VERSION("plc0012", "Requested version is already set as current quote version"),
    CANNOT_PERFORM_MANUALRENEW_OPERATION_SET_TO_TRUE("plc0013", "Cannot perform operation: PolicyDetail.manualRenew has already set to true"),
    CANNOT_PERFORM_MANUALRENEW_OPERATION_SET_TO_FALSE("plc0014", "Cannot perform operation: PolicyDetail.manualRenew has already set to false"),
    PROVIDED_EXPIRATION_DATE_IS_UNEXPECTED_FOR_TERM("plc0015", "Provided Expiration Date.*is unexpected for term .* with Effective Date.*\\. Please, provide expected Expiration Date .* or change term type to CUSTOM"),
    CANNOT_PERFORM_ENDORSEMENT_ISSUE_ACTION_FOR_OOSENDORSEMENT_TRANSACTION_TYPE("plc0016", "Cannot perform endorsementIssue action for OOS_ENDORSEMENT transaction type"),
    CANNOT_PERFORM_DNR_OPERATION_SET_TO_FALSE("plc0017", "Cannot perform operation: PolicyDetail.doNotRenew has already set to false"),
    CANNOT_PERFORM_DNR_OPERATION_SET_TO_TRUE("plc0018", "Cannot perform operation: PolicyDetail.doNotRenew has already set to true"),
    CANNOT_PERFORM_ACTION_AS_POLICY_HAS_CANCELLED_VERSION_TMPL("plc0019", "Cannot perform action as policy has cancelled version at %s"),
    CANNOT_PERFORM_ACTION_AS_POLICY_HAS_RENEWAL("plc0020", "Cannot perform action as policy already has renewal version at .*"),
    DATE_IS_OUTSIDE_OF_THE_POLICY_TERM("plc0022", "Date is outside of the policy term"),
    ENDORSE_EFFECTIVE_EQUAL_OR_LATER_THAN_CANCELLATION_EFFECTIVE("plc0023", "Endorsement effective date cannot be equal to or later than the policy cancellation effective date"),
    USER_ENTER_EFFECTIVE_DATE_LATER_THAN_OR_BEFORE_THAN_ISSUED_ENDORSEMENT_POLICY("plc0024", "Entered date is later or before than effective date of any active policies version"),
    CANNOT_PERFORM_ACTION_ON_NOT_CURRENT_QUOTE_VERSION("plc0025", "Cannot perform action on not current quote version"),
    ENDORSEMENT_ALREADY_EXISTS("plc0027", "Endorsement for this policy term already exists"),
    CANNOT_PERFORM_ACTION_ON_NOT_LATEST_VERSION("plc0028", "Cannot perform action on not latest policy version for the requested date"),
    CANNOT_PERFORM_OPERATION_AS_FLAG_WAS_NOT_SET("plc0029", "Cannot perform operation as flag was not set"),
    REINSTATE_DATE_CANNOT_BE_DIFFERENT_FROM_CANCELLATION("plc0030", "Reinstatement date cannot be different from cancellation date"),
    TERM_EXPIRATION_DATE_MUST_BE_LATER_THAN_EFFECTIVE("plc0031", "Term expiration date must be later than effective date"),
    ROLL_ON_IN_PROGRESS("plc0032", "Command %s can not be performed until out of sequence changes rolled on every affected transaction"),
    NOTHING_TO_ROLL_BACK("plc0033", "There are no active policy versions effective later than the requested one"),
    ATTEMPT_TO_ROLL_BACK_TO_INACTIVE_TX("plc0034", "Cannot perform roll back to the already backed off policy version"),
    NOT_IN_ROLL_ON_PENDING_STATE("plc0035", "There are no versions in roll on pending state for the requested policy"),
    ROLL_ON_NOT_IN_PROGRESS("plc0036", "Command %s can not be performed, roll on process is not in progress"),
    IN_ROLL_ON_PENDING_STATE("plc0037", "Roll on cannot be completed until out of sequence changes rolled on every affected transaction"),
    PACKAGE_IS_NOT_ELIGIBLE("plc0038", "Package %s is not eligible based on the selected dimension values"),
    CHANGING_CUSTOMER_IS_NOT_ALLOWED("plc0039", "Customer could not be changed on %s quote"),
    CUSTOMER_COULD_NOT_BE_CHANGED_ON_THIS_QUOTE_TYPE("plc0039", "Customer could not be changed on %s quote"),
    ATTRIBUTE_HAS_NO_VALUE_FROM_CONFIGURED_LIST("plc0045", "%s has no value %s that is mandatory for the %s"),
    ATTRIBUTES_WITH_CARDINALITY_SINGLE("plc0053", "Can not execute action since some attributes has single cardinality: %s"),
    DUPLICATED_ATTRIBUTE_VALUES("plc0055", "Request contains duplicated attribute values: .*"),
    OFFSET_CANNOT_BE_NEGATIVE_NUMBER("poe0001", "Offset cannot be negative number"),
    LIMIT_CANNOT_BE_LESS_THAN_1("poe0002", "Limit cannot be less than 1"),
    THERE_IS_NO_CONFIGURATION_DEFINED_FOR_TERM_TMPL("pre0001", "There is no configuration defined for term with code %s"),
    NO_ACTIVE_TXS("pte0001", "There is no active policy for the requested date"),
    SELECTED_PACKAGE_IS_NOT_ELIGIBLE("pkg-eligibility-PackagingDetail.packageCd", "Selected package is not eligible"),

    PERSON_DATA_IS_NOT_QUALIFIED("partyuqv0001", "'Person' data is not qualified. Fields used for qualification: (PARTY:firstName, PARTY:lastName, PARTY:birthDate)"),
    ATTRIBUTE_CANNOT_BE_IN_THE_FUTURE("partyve0003", "Attribute '%s' cannot be in the future. Value: '%s'"),

    PREDICATE_NOT_FOUND("rel0001", "Predicate not found : %s"),
    LOCATION_DATA_IS_NOT_QUALIFIED("regyuqv0001", "'Location' data is not qualified. Fields used for qualification: (MAIN:addressLine1, MAIN:city, MAIN:postalCode)"),
    REVERSE_REASON_CD_IS_NOT_AVAILABLE("REVERSE_REASON_CD_IS_NOT_AVAILABLE", "Reverse Reason is not available."),

    ENTITY_COULD_NOT_BE_LOADED("rep0003", "Operation could not be performed as target entity could not be loaded"),
    ENTITY_STREAM_DID_NOT_RETURN_ANY_RESULTS("rep0004", "Operation could not be performed as entity stream did not return any results: The MaybeSource is empty"),

    SAGA_FAILED_ROOT("saga0001", "One or more saga .* steps have failed and have to be manually resumed"),
    SAGA_FAILED_CHILD_STEP("saga0004", "Saga .* step .* execution returned error"),

    USER_DOES_NOT_HAVE_ENOUGH_PRIVILEGES("sec0001", "Request execution failed: User does not have enough privileges"),
    ACCESS_PROFILE_IS_NOT_UNIQUE("secdv0001", "Access profile '%s' is not unique within Security Domain"),
    EFFECTIVE_DATE_AFTER_EXPIRATION("secdv0002", "Access profile '%s' effective date can not be after expiration"),
    CONFIGURED_ACCESS_PROFILE_IS_NOT_CONSISTENT("secdv0003", "Configured Access profile '%s' is not consistent with Access Profile"),
    PARENT_REF_NOT_RESOLVABLE_OR_NOT_EXISTS("secdv0004", "Passed parentRef URI is not resolvable or target entity does not exists"),
    CHANGE_READ_ONLY_PROPERTY("secdv0005", "Passed entity has changed read-only property, expected value is '%s'"),
    USER_PROFILE_EFFECTIVE_DATE_AFTER_EXPIRATION("secdv0005", "User profile expiration date can not be before effective"),
    MULTIPLE_CONFIGURED_ACCESS_PROFILES("secdv0006", "Multiple configured access profiles [%s, %s] can't be based on same access profile %s"),
    DIMENSION_VALUE_IS_MANDATORY("secdv0008", "Dimension value is mandatory for restricted dimensions"),
    ONE_OF_PROFILE_SHOULD_BE_MARKED_AS_DEFAULT("secuv0001", "Multiple user profiles effective on the same date are not allowed. One of profiles should be marked as default."),

    SPECIFIED_TRANSACTION_ALREADY_WAIVED("SPECIFIED_TRANSACTION_ALREADY_WAIVED", "Specified transaction is already waived"),
    SPECIFIED_TRANSACTION_DOES_NOT_HAVE_TRANSFER_ACTION("SPECIFIED_TRANSACTION_DOES_NOT_HAVE_TRANSFER_ACTION", "Specified transaction with id - #%s does not have transfer action"),

    UNKNOWN_SEARCH_CRITERIA_FIELD("sgw0003", "Unknown search criteria field 'UNKNOWNCRITERIA'"),
    MATCHES_CONDITION_FOR_FIELD_SUPPLIED_WITH_NO_OR_INVALID_VALUES("sgw0005", "Matches condition for field '.*' supplied with no or invalid values"),

    FIELD_IS_NOT_UNIQUE("unf0001", "Field '%s' is not unique"),
    USER_HAS_NO_STATES_ASSOCIATED("usfe0002", "User has no states associated"),

    EMAIL_ADDRESS_IS_REQUIRED("422", "Email Address is required"),
    BIRTH_DATA_VALIDATION("422", "Date of Birth cannot be after the current date, and cannot be more than 120 years before the current date."),
    ZIP_CODE_VALIDATION("422", "Zip Code is required for customer"),
    PHONE_NUMBER_VALIDATION("422","Phone Number is required"),
    EMAIL_VALIDATION("422", "Entered email is not valid. Please enter valid email"),

    VALIDATION_ERROR_REQUEST_WELL_FORMED("VALIDATION_ERROR", "The request was well-formed but was unable to be followed due to semantic errors (invalid field value, validation rules and etc)"),
    WRONG_ADV_ALLOCATION_AMOUNT("WRONG_ADV_ALLOCATION_AMOUNT", "Balance amount does not match advanced allocations for policy number %S, eff. date .*"),

    //verify labels
    QUICKQUOTE_AUTO_PACKAGECD("PACKAGECD_IS_REQUIRED", "packageCd is required.");

    private final String code;
    private final String message;

    ErrorConstant(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getCode(), getMessage());
    }

}
