package com.onarinskyi.annotations.ui;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Autowired
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PageComponent {
    String id() default "";
    String name() default "";
    String css() default "";
    String xpath() default "";
    String text() default "";
}
