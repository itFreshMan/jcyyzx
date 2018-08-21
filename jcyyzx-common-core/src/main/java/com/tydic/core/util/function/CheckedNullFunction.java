package com.tydic.core.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Function with no arguments.
 *
 * @author Guang YANG
 */
@FunctionalInterface
public interface CheckedNullFunction<R> {

  R apply() throws Exception;

  default <V> CheckedNullFunction<V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return () -> {
      R r = apply();
      return after.apply(r);
    };
  }
}
