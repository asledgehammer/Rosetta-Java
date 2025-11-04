package com.asledgehammer.rosetta.java.reference;

import java.lang.reflect.*;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class CandleReference {

  public static void clearCache() {
    TypeReference.clearCache();
    ClassReference.clearCache();
  }

  @NotNull
  public static ClassReference of(@NotNull Class<?> clazz) {
    return ClassReference.of(clazz);
  }

  @NotNull
  public static TypeReference of(@NotNull TypeVariable<?> typeVariable) {
    return TypeReference.of(typeVariable);
  }

  @NotNull
  public static TypeReference of(@NotNull Type type) {
    return TypeReference.of(type);
  }

  @NotNull
  public static FieldReference of(@NotNull Field field) {
    ClassReference classReference = of(field.getDeclaringClass());
    return classReference.getFieldReference(field);
  }

  @NotNull
  public static MethodReference of(@NotNull Method method) {
    ClassReference classReference = of(method.getDeclaringClass());
    return classReference.getMethodReference(method);
  }

  @NotNull
  public static ConstructorReference of(@NotNull Constructor<?> constructor) {
    ClassReference classReference = of(constructor.getDeclaringClass());
    return classReference.getConstructorReference(constructor);
  }
}
