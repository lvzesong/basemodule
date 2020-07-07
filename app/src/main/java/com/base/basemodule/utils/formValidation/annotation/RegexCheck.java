package com.base.basemodule.utils.formValidation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)  
@Retention(RetentionPolicy.RUNTIME)
public @interface RegexCheck {
	String message();
	String value() default "";//正则匹配
}
