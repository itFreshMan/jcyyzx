package com.tydic.core.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Function with 5 arguments.
 *
 * @author Guang YANG
 */
@FunctionalInterface
public interface QuintFunction<A1, A2, A3, A4, A5, R> {

  R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5);

  default <V> QuintFunction<A1, A2, A3, A4, A5, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (A1 a1, A2 a2, A3 a3, A4 a4, A5 a5) -> {
      R r = apply(a1, a2, a3, a4, a5);
      return after.apply(r);
    };
  }
}
