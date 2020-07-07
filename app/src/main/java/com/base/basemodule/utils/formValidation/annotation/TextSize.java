package com.base.basemodule.utils.formValidation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)  
@Retention(RetentionPolicy.RUNTIME)
public @interface TextSize {
	String message();
	int minlength() default 0;   //最小长度
    int maxlength() default 0;   //最大长度
}
