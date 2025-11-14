package com.asledgehammer.rosetta.java;

import org.jetbrains.annotations.Nullable;

/**
 * The ReflectedReferenceable interface is for Rosetta-Java objects that links with a Rosetta
 * definition.
 *
 * @param <E> The type of Reflection object linked.
 */
public interface Reflected<E> {

  /**
   * @return The Java Reflection object linked with the definition.
   */
  @Nullable
  E getReflectionTarget();

  /**
   * @return True if the reflection-target is assigned.
   */
  default boolean isReflectionTargetLinked() {
    return getReflectionTarget() != null;
  }
}
