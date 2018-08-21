package com.tydic.core.util.collection;


import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Guang YANG
 */
public class EnhancedArrayList<E> extends ArrayList<E> implements EnhancedList<E> {

  private static final long serialVersionUID = -7331558616214269785L;

  public EnhancedArrayList() {
    super();
  }

  public EnhancedArrayList(Collection<? extends E> c) {
    super(c);
  }

  @Override
  public EnhancedList<E> first(int limit) {
    EnhancedList<E> copy = this.clone();
    if (limit < 0 || limit > copy.size() - 1) {
      return copy;
    }
    return new EnhancedArrayList<>(copy.subList(0, limit));
  }

  @Override
  @SuppressWarnings("unchecked")
  public EnhancedArrayList<E> clone() {
    return (EnhancedArrayList) super.clone();
  }

}
