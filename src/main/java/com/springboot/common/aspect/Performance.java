package com.springboot.common.aspect;


import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@SuppressWarnings("all")
public @interface Performance {

}
