package com.asledgehammer.rosetta.java;

import org.jetbrains.annotations.NotNull;

public enum JavaScope {
  PUBLIC("public"),
  PACKAGE("package"),
  PROTECTED("protected"),
  PRIVATE("private");

  private final @NotNull String id;

  JavaScope(@NotNull String id) {
    this.id = id;
  }

  @NotNull
  public String getID() {
    return id;
  }

  /**
   * @param id The string ID of the type.
   * @return The JavaScope.
   * @throws NullPointerException If the ID is null.
   * @throws IllegalArgumentException If the ID is invalid.
   */
  @NotNull
  public static JavaScope of(@NotNull String id) {
    return switch (id) {
      case "public" -> PUBLIC;
      case "package" -> PACKAGE;
      case "protected" -> PROTECTED;
      case "private" -> PRIVATE;
      default -> throw new IllegalArgumentException("Unknown JavaScope: " + id);
    };
  }
}
