package com.tydic.core.util;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A utility to handle the reflections.
 *
 * @author Guang YANG
 * @version 1.0
 */
public final class Reflections {

  private static Logger LOGGER = LoggerFactory.getLogger(Reflections.class);

  private Reflections() {
    throw new AssertionError(Strings.INSTANTIATION_PROHIBITED);
  }

  /**
   * Copy property values from the origin bean to the destination bean
   * for all cases where the property names are the same.
   *
   * @see org.springframework.beans.BeanUtils#copyProperties(Object, Object)
   * @see org.apache.commons.beanutils.BeanUtils#copyProperties(Object, Object)
   */
  public static <D, S> D transform(S source, Class<D> targetClass) {
    if (source == null || targetClass == null) {
      return null;
    }
    try {
      D desc = targetClass.newInstance();
      try {
        BeanUtils.copyProperties(desc, source);
      } catch (Exception copy) {
        LOGGER.debug(copy.getMessage());
      }
      return desc;
    } catch (Exception ex) {
      LOGGER.debug(ex.getMessage());
      return null;
    }
  }

  /**
   * Populate attributes to an instance of a specific Java Bean class from a java.util.Map instance.
   */
  public static <K, V, T> T populate(Map<K, V> data, Class<T> clazz) {
    if (data == null || clazz == null) {
      return null;
    }
    Map<String, Object> m = new LinkedHashMap<>();
    data.forEach((k, v) -> m.put(Strings.of(k), v));
    try {
      T bean = clazz.newInstance();
      BeanUtils.populate(bean, m);
      return bean;
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      LOGGER.debug(e.getMessage());
      return null;
    }
  }

  /**
   * Populate attributes to a java.util.Map from a Java Bean.
   */
  public static Map<String, Object> populate(Object bean) {
    if (bean == null) {
      return null;
    }
    Map<String, Object> data = new LinkedHashMap<>();
    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      for (PropertyDescriptor property : propertyDescriptors) {
        String key = property.getName();
        if ("class".equals(key)) {
          continue;
        }
        Method getter = property.getReadMethod();
        Object value = getter.invoke(bean);
        data.put(key, value);
      }
      return data;
    } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
      LOGGER.debug(e.getMessage());
      return null;
    }
  }

  /**
   * @see Object#toString()
   */
  public static String toString(Object entity) {
    if (entity == null) {
      return null;
    }
    if (entity.getClass().isArray()) {
      return Strings.ofArray(entity);
    }
    if (entity instanceof List || entity instanceof Map) {
      return entity.toString();
    }
    StringBuilder builder = new StringBuilder();
    builder.append(entity.getClass().getName()).append("{");
    for (Field f : entity.getClass().getDeclaredFields()) {
      if (Modifier.isStatic(f.getModifiers())) {
        continue;
      }
      builder.append(f.getName()).append(": ");
      Object value = null;
      try {
        f.setAccessible(true);
        value = f.get(entity);
      } catch (IllegalAccessException e) {
        // Doing nothing
      }
      builder.append(value).append(",");
    }
    if (builder.lastIndexOf(",") == builder.length() - 1) {
      builder.deleteCharAt(builder.length() - 1);
    }
    builder.append("}");
    return builder.toString();
  }

  /**
   * Get parameterized types of a class.
   */
  public static Class[] getParameterizedTypes(Class<?> clazz) {
    ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
    Type[] types = type.getActualTypeArguments();
    Class[] classes = new Class[types.length];
    Loop.forEach(types, (i, e) -> {
      if (e instanceof Class) {
        classes[i] = (Class) e;
      } else {
        // should it be an Object if it's something like <T>?
        classes[i] = Object.class;
      }
    });
    return classes;
  }

  /**
   * Get the first one parameterized type of a class.
   */
  public static Class getParameterizedType(Class<?> clazz) {
    Class[] types = Reflections.getParameterizedTypes(clazz);
    if (types.length == 0) {
      return null;
    }
    return types[0];
  }


  public static String getter(String field) {
    Objects.requireNonNull(field);
    return "get" + Strings.capitalize(field);
  }

  public static String setter(String field) {
    Objects.requireNonNull(field);
    return "set" + Strings.capitalize(field);
  }

  public static Method getter(Object object, String field) {
    Objects.requireNonNull(object);
    return getter(object.getClass(), field);
  }

  public static Method setter(Object object, String field) {
    Objects.requireNonNull(object);
    return setter(object.getClass(), field);
  }

  public static Method getter(Class<?> clazz, String field) {
    Objects.requireNonNull(clazz);
    try {
      return clazz.getDeclaredMethod(getter(field));
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  public static Method setter(Class<?> clazz, String field) {
    Objects.requireNonNull(clazz);
    String setter = setter(field);
    try {
      Field f = clazz.getDeclaredField(field);
      return clazz.getDeclaredMethod(setter, f.getDeclaringClass());
    } catch (NoSuchFieldException | NoSuchMethodException e) {
      return null;
    }
  }

  public static Object invokeGetter(Object obj, String propertyName) {
    String getterMethodName = getter(propertyName);
    return invokeMethod(obj, getterMethodName, new Class[]{}, new Object[]{});
  }

  public static void invokeSetter(Object obj, String propertyName, Object value) {
    invokeSetter(obj, propertyName, value, null);
  }

  public static void invokeSetter(Object obj, String propertyName, Object value, Class<?> propertyType) {
    Class<?> type = propertyType != null ? propertyType : value.getClass();
    String setterMethodName = setter(propertyName);
    invokeMethod(obj, setterMethodName, new Class[]{type}, new Object[]{value});
  }

  public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
      final Object[] args) {
    Method method = getAccessibleMethod(obj, methodName, parameterTypes);
    Objects.requireNonNull(method);
    try {
      return method.invoke(obj, args);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
    Objects.requireNonNull(obj);
    for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
      try {
        Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
      } catch (NoSuchMethodException e) {
        continue;
      }
    }
    return null;
  }
}
