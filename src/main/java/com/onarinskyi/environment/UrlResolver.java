package com.onarinskyi.environment;

import com.onarinskyi.annotations.api.*;
import com.onarinskyi.annotations.ui.Url;
import com.onarinskyi.api.Request;
import com.onarinskyi.gui.Page;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Component
@PropertySources({
        @PropertySource("classpath:driver.properties"),
        @PropertySource("classpath:api.properties")
})

public class UrlResolver {

    private final Logger logger = Logger.getLogger(this.getClass());

    private String uiBaseUrl;
    private List<String> apiBaseUrls;

    @Autowired
    public UrlResolver(Environment environment, @Value("#{systemProperties['env']}") String usedEnv) {
        try {
            switch (usedEnv == null ? "" : usedEnv) {
                case "":
                    uiBaseUrl = environment.getProperty("default.base.url");
                    apiBaseUrls = Arrays.asList(environment.getProperty("default.api.url").split(","));
                    break;
                default:
                    uiBaseUrl = environment.getProperty(usedEnv.concat(".base.url"));
                    apiBaseUrls = Arrays.asList(environment.getProperty(usedEnv.concat(".api.url")).split(","));
                    break;
            }
        } catch (Exception e) {
            logger.error("Check your env system variable");
            throw e;
        }
    }

    public String getUiBaseUrl() {
        return uiBaseUrl;
    }

    public List<String> getApiBaseUrls() {
        return apiBaseUrls;
    }

    public String getResolvedUrlFor(Object object) {
        try {
            return getPropertiesBasedUrlFor(object);
        } catch (MalformedURLException e1) {
            logger.warn("Application base url is malformed. Please review it!");
            try {
                return getClassAnnotationBasedUrl(object);
            } catch (MalformedURLException e2) {
                logger.warn("Application url in class: " + object.getClass().getSimpleName() + " is malformed. Please review it!");
                throw new RuntimeException("No valid URL was found in page declaration or properties file");
            }
        }
    }

    private String getPropertiesBasedUrlFor(Object object) throws MalformedURLException {
        String baseUrl;

        if (object instanceof Page) {
            baseUrl = uiBaseUrl;
        } else if (object instanceof Request) {
            String partialUrl = getPartialUrl(object.getClass());
            baseUrl = partialUrl.isEmpty() ?
                    apiBaseUrls.get(0) :
                    apiBaseUrls.stream().filter(v -> v.contains(partialUrl)).findFirst().get();
        } else {
            throw new MalformedURLException("Unknown object requesting url");
        }

        baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";

        new URL(baseUrl);

        String urlAnnotation = getUrlAnnotationValue(object.getClass());

        urlAnnotation = urlAnnotation.startsWith("/") ? urlAnnotation.substring(1) : urlAnnotation;

        String fullUrl = urlAnnotation.contains("http") ?
                baseUrl + urlAnnotation.substring(urlAnnotation.lastIndexOf("/")) :
                baseUrl + urlAnnotation;

        new URL(fullUrl);

        return fullUrl;
    }

    private String getClassAnnotationBasedUrl(Object object) throws MalformedURLException {
        return new URL(getUrlAnnotationValue(object.getClass())).toString();
    }

    private String getUrlAnnotationValue(Class<?> clazz) {
        return clazz.isAnnotationPresent(Url.class) ?
                clazz.getAnnotation(Url.class).value() :
                clazz.isAnnotationPresent(Get.class) ?
                        clazz.getAnnotation(Get.class).endpoint() :
                        clazz.isAnnotationPresent(Put.class) ?
                                clazz.getAnnotation(Put.class).endpoint() :
                                clazz.isAnnotationPresent(Post.class) ?
                                        clazz.getAnnotation(Post.class).endpoint() :
                                        clazz.isAnnotationPresent(Delete.class) ?
                                                clazz.getAnnotation(Delete.class).endpoint() : "";
    }

    private String getPartialUrl(Class<?> clazz) {
        return clazz.isAnnotationPresent(Get.class) ?
                clazz.getAnnotation(Get.class).api() :
                clazz.isAnnotationPresent(Put.class) ?
                        clazz.getAnnotation(Put.class).api() :
                        clazz.isAnnotationPresent(Post.class) ?
                                clazz.getAnnotation(Post.class).api() :
                                clazz.isAnnotationPresent(Delete.class) ?
                                        clazz.getAnnotation(Delete.class).api() :
                                        clazz.isAnnotationPresent(Head.class) ?
                                                clazz.getAnnotation(Head.class).api() :
                                                clazz.isAnnotationPresent(Options.class) ?
                                                        clazz.getAnnotation(Options.class).api() : "";
    }
}
