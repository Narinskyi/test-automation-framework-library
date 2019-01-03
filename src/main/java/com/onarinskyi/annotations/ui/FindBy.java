package com.onarinskyi.annotations.ui;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FindBy {
    String id() default "";
    String name() default "";
    String css() default "";
    String xpath() default "";
    String text() default "";
}
