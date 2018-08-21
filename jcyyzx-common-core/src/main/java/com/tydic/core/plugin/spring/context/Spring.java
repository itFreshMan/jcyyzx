package com.tydic.core.plugin.spring.context;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;

import static com.tydic.core.util.Strings.INSTANTIATION_PROHIBITED;

/**
 * A utility to get a registered bean form Spring context.
 *
 * @author Guang YANG
 */
public final class Spring {

  private volatile static ApplicationContext CTX;

  private Spring() {
    throw new AssertionError(INSTANTIATION_PROHIBITED);
  }

  /**
   * @see org.springframework.context.ApplicationContext#getBean(String)
   */
  public static Object get(String beanName) {
    checkInstance();
    return CTX.getBean(beanName);
  }

  /**
   * @see org.springframework.context.ApplicationContext#getBean(Class)
   */
  public static <T> T get(Class<T> beanType) {
    checkInstance();
    return CTX.getBean(beanType);
  }

  /**
   * @see org.springframework.context.ApplicationContext#getBean(String, Class)
   */
  public static <T> T get(String beanName, Class<T> beanType) {
    checkInstance();
    return CTX.getBean(beanName, beanType);
  }

  private static void checkInstance() {
    if (CTX != null) {
      return;
    }
    synchronized (Spring.class) {
      if (CTX == null) {
        CTX = ApplicationContextHolder.get();
      }
    }
  }

  /**
   * @see org.springframework.beans.factory.BeanFactoryUtils#beanOfType(ListableBeanFactory, Class, boolean, boolean)
   */
  public <T> T get(Class<T> beanType, boolean includeNonSingletons, boolean allowEagerInit) {
    checkInstance();
    return BeanFactoryUtils.beanOfType(CTX, beanType, includeNonSingletons, allowEagerInit);
  }
}
