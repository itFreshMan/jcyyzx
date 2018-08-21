package com.tydic.core.annotation;

import java.lang.annotation.*;

/**
 * 记录日志
 *
 * @author Guang YANG
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {

  /**
   * 是否记录错误日志
   */
  boolean exceptional() default true;

  /**
   * 是否记录操作日志
   */
  boolean operational() default false;

  /**
   * 方法描述
   */
  String desc() default "";

  /**
   * 方法描述
   *
   * @see #desc()
   */
  String value();
}
