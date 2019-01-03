package com.onarinskyi.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.testng.Assert;

public abstract class Response {

    private Logger log = Logger.getLogger(this.getClass());

    @JsonIgnore
    protected String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Parsing request object to JSON failed");
            Assert.fail(e.getMessage());
        }
        return "";
    }
}
