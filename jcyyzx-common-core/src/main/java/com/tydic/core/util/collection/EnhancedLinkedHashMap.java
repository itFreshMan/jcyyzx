package com.tydic.core.util.collection;

import java.util.LinkedHashMap;

/**
 * @author Guang YANG
 */
public class EnhancedLinkedHashMap<K, V> extends LinkedHashMap<K, V> implements EnhancedMap<K, V> {

  private static final long serialVersionUID = -9115034395997341576L;

  @Override
  @SuppressWarnings("unchecked")
  public EnhancedMap<K, V> clone() {
    return (EnhancedMap) super.clone();
  }
}
