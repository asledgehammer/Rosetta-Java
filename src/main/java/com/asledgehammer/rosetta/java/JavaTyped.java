package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.java.reference.TypeReference;
import org.jetbrains.annotations.NotNull;

public interface JavaTyped {
  @NotNull
  TypeReference getType();

  void setType(@NotNull TypeReference type);
}
