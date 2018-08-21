package com.tydic.core.util.function;

/**
 * @author Guang YANG
 */
@FunctionalInterface
public interface CheckedRunnable {

  void run() throws Exception;
}
