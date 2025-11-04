package com.asledgehammer.reference;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ParameterReference<E extends ExecutableReference<?>> {

  private final TypeReference resolvedType;
  private final Parameter parameter;
  private final Type genericType;
  private final E executableReference;

  ParameterReference(
      @NotNull E executableReference, @NotNull Parameter parameter, @NotNull Type genericType) {
    this.executableReference = executableReference;
    this.parameter = parameter;
    this.genericType = genericType;

    ClassReference classReference = executableReference.getClassReference();
    Class<?> deCl = executableReference.getExecutable().getDeclaringClass();
    this.resolvedType = classReference.resolveType(genericType, deCl);
  }

  public TypeReference getResolvedType() {
    return resolvedType;
  }

  public ExecutableReference<?> getExecutableReference() {
    return executableReference;
  }

  public Parameter getParameter() {
    return parameter;
  }

  public Type getGenericType() {
    return genericType;
  }
}
