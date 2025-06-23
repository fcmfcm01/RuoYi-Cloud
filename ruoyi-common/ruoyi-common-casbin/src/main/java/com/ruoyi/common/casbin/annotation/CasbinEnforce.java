package com.ruoyi.common.casbin.annotation;

import java.lang.annotation.*;

/**
 * Casbin权限验证注解
 * 
 * @author ruoyi
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CasbinEnforce
{
    /**
     * 资源对象，支持SpEL表达式
     */
    String object() default "";

    /**
     * 操作动作，支持SpEL表达式
     */
    String action() default "";

    /**
     * 主体，默认为当前登录用户，支持SpEL表达式
     */
    String subject() default "";

    /**
     * 权限验证失败时的错误消息
     */
    String message() default "权限不足";
}