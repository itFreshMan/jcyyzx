package com.tydic.core.util.function;

import java.util.Objects;

/**
 * Consumer with 5 arguments.
 *
 * @author Guang YANG
 */
@FunctionalInterface
public interface QuintConsumer<T1, T2, T3, T4, T5> {

  void accept(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

  default QuintConsumer<T1, T2, T3, T4, T5> andThen(QuintConsumer<? super T1, ? super T2, ? super T3, ? super T4, ? super T5> after) {
    Objects.requireNonNull(after);
    return (t1, t2, t3, t4, t5) -> {
      accept(t1, t2, t3, t4, t5);
      after.accept(t1, t2, t3, t4, t5);
    };
  }
}
