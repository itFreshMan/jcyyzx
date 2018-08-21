package com.tydic.core.plugin.spring;

import com.tydic.core.plugin.spring.context.EnvironmentHolder;
import com.tydic.core.plugin.spring.context.Resolver;
import com.tydic.core.util.collection.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.tydic.core.util.Strings.INSTANTIATION_PROHIBITED;

/**
 * Read properties in a Properties or a Yaml file.
 *
 * @author Guang YANG
 * @version 1.0
 */
public final class PropertyUtils {

  private static final Map<String, Properties> cachePropertiesMap = new ConcurrentHashMap<>();
  private static Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);
  private volatile static Environment ENV;

  private PropertyUtils() {
    throw new AssertionError(INSTANTIATION_PROHIBITED);
  }

  public static String getProperty(String key) {
    Objects.requireNonNull(key);
    checkInstance();
    return ENV != null ? ENV.getProperty(key) : System.getProperty(key);
  }

  public static Properties getProperties(String locationPattern) {
    return cachedRead(Objects.requireNonNull(locationPattern));
  }

  public static String getProperty(String locationPattern, String key) {
    return getProperties(locationPattern).getProperty(Objects.requireNonNull(key));
  }

  private static void checkInstance() {
    if (ENV != null) {
      return;
    }
    synchronized (PropertyUtils.class) {
      if (ENV == null) {
        ENV = EnvironmentHolder.get();
      }
    }
  }

  private static Properties cachedRead(String locationPattern) {
    Properties properties = cachePropertiesMap.get(locationPattern);
    if (properties == null) {
      properties = read(locationPattern);
      cachePropertiesMap.put(locationPattern, properties);
    }
    return properties;
  }

  private static Properties read(String locationPattern) {
    Resource resource = Resolver.getResource(locationPattern);
    Properties result = new Properties();
    Lists.of(readProperties(resource), readYaml(resource)).forEach(p -> {
      if (p != null) {
        result.putAll(p);
      }
    });
    return result;
  }

  private static Properties readProperties(Resource resource) {
    try {
      PropertiesFactoryBean factoryBean = new PropertiesFactoryBean();
      factoryBean.setSingleton(false);
      factoryBean.setLocation(resource);
      factoryBean.afterPropertiesSet();
      return factoryBean.getObject();
    } catch (Exception e) {
      LOGGER.debug(e.getMessage());
      return null;
    }
  }

  private static Properties readYaml(Resource resource) {
    try {
      YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
      factoryBean.setSingleton(false);
      factoryBean.setResources(resource);
      factoryBean.afterPropertiesSet();
      return factoryBean.getObject();
    } catch (Exception e) {
      LOGGER.debug(e.getMessage());
      return null;
    }
  }

}
