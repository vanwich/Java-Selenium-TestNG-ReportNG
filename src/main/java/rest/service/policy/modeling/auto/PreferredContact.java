package rest.service.policy.modeling.auto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import help.data.TestData;
import help.ws.rest.model.Adjustable;
import help.ws.rest.model.DXPModel;

@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreferredContact extends DXPModel implements Adjustable<PreferredContact> {
    private String oid;
    private String firstName;
    private String lastName;
    private String additionalInsured;

    public PreferredContact(){}

    public PreferredContact(TestData td){
        super(td);
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAdditionalInsured() {
        return additionalInsured;
    }

    public void setAdditionalInsured(String additionalInsured) {
        this.additionalInsured = additionalInsured;
    }
}
