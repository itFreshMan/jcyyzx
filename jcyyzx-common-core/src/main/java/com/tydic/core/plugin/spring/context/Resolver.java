package com.tydic.core.plugin.spring.context;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

import static com.tydic.core.util.Strings.INSTANTIATION_PROHIBITED;

/**
 * An easy-to-use tool for getting resources from a specific location.
 *
 * @author Guang YANG
 * @version 1.0
 * @see org.springframework.core.io.Resource
 * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
 */
public final class Resolver {

  private static ResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();

  private Resolver() {
    throw new AssertionError(INSTANTIATION_PROHIBITED);
  }

  /**
   * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver#getResources(String)
   */
  public static Resource[] getResources(String locationPattern) throws IOException {
    return RESOLVER.getResources(locationPattern);
  }

  /**
   * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver#getResource(String)
   */
  public static Resource getResource(String locationPattern) {
    return RESOLVER.getResource(locationPattern);
  }
}
