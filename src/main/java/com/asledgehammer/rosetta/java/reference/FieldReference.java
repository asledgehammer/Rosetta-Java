package com.asledgehammer.rosetta.java.reference;

import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class FieldReference {

  private final ClassReference classReference;
  private final Field field;
  private final TypeReference type;

  FieldReference(@NotNull ClassReference classReference, @NotNull Field field) {
    this.classReference = classReference;
    this.field = field;
    this.type = classReference.resolveType(field.getGenericType(), field.getDeclaringClass());
  }

  @NotNull
  public ClassReference getClassReference() {
    return classReference;
  }

  @NotNull
  public Field getField() {
    return field;
  }

  @NotNull
  public TypeReference getType() {
    return type;
  }
}
