package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.java.reference.TypeReference;
import com.asledgehammer.rosetta.RosettaObject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Map;

public class JavaField extends RosettaObject implements ReflectedReferenceable<Field> {

  private final Field reflectedObject;

  private TypeReference type;

  JavaField(@NotNull Field field) {
    super();

    this.reflectedObject = field;
    this.type = TypeReference.of(field.getGenericType());
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
  }

  @Override
  public @NotNull Field getReflectedObject() {
    return this.reflectedObject;
  }
}
