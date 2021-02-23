
package com.mygoodbot.stackexchange;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "disclaimer",
    "license",
    "timestamp",
    "base",
    "rates"
})
public class RatesResponse {

    @JsonProperty("disclaimer")
    public String disclaimer;
    @JsonProperty("license")
    public String license;
    @JsonProperty("timestamp")
    public Integer timestamp;
    @JsonProperty("base")
    public String base;
    @JsonProperty("rates")
//    public Rates rates;
    public Map<String, Object> rates = new HashMap<String, Object>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("disclaimer")
    public String getDisclaimer() {
        return disclaimer;
    }

    @JsonProperty("disclaimer")
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    @JsonProperty("license")
    public String getLicense() {
        return license;
    }

    @JsonProperty("license")
    public void setLicense(String license) {
        this.license = license;
    }

    @JsonProperty("timestamp")
    public Integer getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("base")
    public String getBase() {
        return base;
    }

    @JsonProperty("base")
    public void setBase(String base) {
        this.base = base;
    }

    @JsonProperty("rates")
    public Map<String, Object> getRates() {
        return rates;
    }
//    public Rates getRates() {
//        return rates;
//    }

    @JsonProperty("rates")
    public void setRates(Map<String, Object> rates) {
        this.rates = rates;
    }
//    public void setRates(Rates rates) {
//        this.rates = rates;
//    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
