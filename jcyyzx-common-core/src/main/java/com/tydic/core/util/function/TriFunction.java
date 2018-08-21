package com.tydic.core.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Function with 3 arguments.
 *
 * @author Guang YANG
 */
@FunctionalInterface
public interface TriFunction<A1, A2, A3, R> {

  R apply(A1 a1, A2 a2, A3 a3);

  default <V> TriFunction<A1, A2, A3, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (A1 a1, A2 a2, A3 a3) -> {
      R r = apply(a1, a2, a3);
      return after.apply(r);
    };
  }
}
