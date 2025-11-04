package com.asledgehammer.rosetta.java;

import com.asledgehammer.reference.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaGenericParameter {

  private TypeReference type;
  private String notes;

  public JavaGenericParameter(@NotNull TypeReference type) {
    this.type = type;
  }

  public TypeReference getType() {
    return type;
  }

  public void setType(TypeReference type) {
    this.type = type;
  }

  @Nullable
  public String getNotes() {
    return this.notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }
}
