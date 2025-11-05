package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.java.reference.TypeReference;
import com.asledgehammer.rosetta.RosettaObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Map;

public class JavaReturn extends RosettaObject {

  private TypeReference type;

  private String notes;

  public JavaReturn(@NotNull Type type) {
    this.type = TypeReference.of(type);
  }

  @Override
  public boolean onCompile() {
    return true;
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {}

  @NotNull
  @Override
  protected Map<String, Object> onSave() {
    return Map.of();
  }

  @NotNull
  public TypeReference getType() {
    return this.type;
  }

  public void setType(@NotNull TypeReference type) {
    this.type = type;
    setDirty();
  }

  @Nullable
  public String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }
}
