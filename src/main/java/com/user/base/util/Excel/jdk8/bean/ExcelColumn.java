package com.user.base.util.Excel.jdk8.bean;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    String value() default "";

    int col() default 0;
}
