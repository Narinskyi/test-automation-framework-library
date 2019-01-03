package com.onarinskyi.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onarinskyi.annotations.api.*;
import com.onarinskyi.environment.UrlResolver;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Component
public abstract class Request {

    @JsonIgnore
    private Logger log = Logger.getLogger(this.getClass());

    @JsonIgnore
    protected Map<String, String> headers = new HashMap<>();

    @JsonIgnore
    protected Map<String, String> parameters = new HashMap<>();

    @JsonIgnore
    protected RequestSpecification requestSpecification;

    @JsonIgnore
    @Autowired
    private UrlResolver urlResolver;

    @Step("Sending request")
    public synchronized <T extends Response> T sendAndExpect(Class<T> responseClass) {

        T response = null;

        try {
            requestSpecification = requestSpecification == null ?
                    given()
                            .relaxedHTTPSValidation()
                            .auth().none()
                            .contentType("application/json")
                            .headers(headers)
                            .body(this) :

                    requestSpecification;

            parameters.keySet().forEach(key -> requestSpecification.queryParam(key, parameters.get(key)));

            String url = urlResolver.getResolvedUrlFor(this);

            String responseBody = sendRequest(requestSpecification, url);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            response = mapper.readValue(responseBody, responseClass);
            response.setBody(responseBody);
        } catch (IOException e) {
            log.error("Reading response object failed");
            Assert.fail(e.getMessage());
        }

        attach(this);
        attach(response);

        return response;
    }

    private String sendRequest(RequestSpecification request, String url) {
        log.info("Sending request: " + this.getClass().getSimpleName() + " to: " + url);

        Class clazz = this.getClass();

        return clazz.isAnnotationPresent(Get.class) ?
                request.get(url).getBody().asString() :
                clazz.isAnnotationPresent(Put.class) ?
                        request.put(url).getBody().toString() :
                        clazz.isAnnotationPresent(Post.class) ?
                                request.post(url).getBody().toString() :
                                clazz.isAnnotationPresent(Delete.class) ?
                                        request.delete(url).getBody().toString() :
                                        clazz.isAnnotationPresent(Head.class) ?
                                                request.head(url).getBody().toString() :
                                                clazz.isAnnotationPresent(Options.class) ?
                                                        request.options(url).getBody().toString() : "";
    }

    @Attachment(type = "application/json", value = "Request")
    private String attach(Request request) {
        return request.toString();
    }

    @Attachment(type = "application/json", value = "Response")
    private String attach(Response response) {
        return response.toString();
    }

    public <T extends Request> T withHeaders(Map<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public <T extends Request> T withParameter(String key, String value) {
        this.parameters.put(key, value);
        return (T) this;
    }

    public <T extends Request> T withRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
        return (T) this;
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
