package com.asledgehammer.rosetta;

import org.jetbrains.annotations.NotNull;

/**
 * The Reflected interface is for Rosetta-Java objects that links with a Rosetta definition.
 *
 * @param <E> The type of Reflection object linked.
 */
public interface Reflected<E> {

  /**
   * @return The Java Reflection object linked with the definition.
   */
  @NotNull
  E getReflectedObject();
}
