package com.tydic.core.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Function with 4 arguments.
 *
 * @author Guang YANG
 */
@FunctionalInterface
public interface QuadFunction<A1, A2, A3, A4, R> {

  R apply(A1 a1, A2 a2, A3 a3, A4 a4);

  default <V> QuadFunction<A1, A2, A3, A4, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (A1 a1, A2 a2, A3 a3, A4 a4) -> {
      R r = apply(a1, a2, a3, a4);
      return after.apply(r);
    };
  }
}
