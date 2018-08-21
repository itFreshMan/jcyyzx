package com.tydic.core.plugin.spring.context;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Get an Environment instance of the current spring context.
 * Can be used to read properties of the application.
 *
 * NOTICE: NEVER USE IT WHEN REGISTERING A SPRING BEAN.
 *
 * @author Guang YANG
 * @version 1.0
 * @see Environment
 */
@Component
public class EnvironmentHolder implements EnvironmentAware {

  private static Environment ENV;

  public static Environment get() {
    return ENV;
  }

  @Override
  public void setEnvironment(Environment environment) {
    ENV = environment;
  }
}
