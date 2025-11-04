package com.asledgehammer.rosetta;

import org.jetbrains.annotations.NotNull;

/** NamedEntities identify and stores a name. */
public interface NamedEntity {
  /**
   * @return The formal name of the entity.
   */
  @NotNull
  String getName();
}
