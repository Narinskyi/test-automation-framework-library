package com.onarinskyi.annotations.api;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Autowired
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Request {}
