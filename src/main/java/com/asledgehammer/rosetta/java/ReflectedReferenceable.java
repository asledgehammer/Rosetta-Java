package com.asledgehammer.rosetta.java;

import org.jetbrains.annotations.Nullable;

/**
 * The ReflectedReferenceable interface is for Rosetta-Java objects that links with a Rosetta definition.
 *
 * @param <E> The type of Reflection object linked.
 */
public interface ReflectedReferenceable<E> {

  /**
   * @return The Java Reflection object linked with the definition.
   */
  @Nullable
  E getReflectedObject();
}
