package com.acme.acmemall.annotation;

import java.lang.annotation.*;

/**
 * 忽略Token验证
 *
 * @author admin
 *
 * @date 2023-02-07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {

}
