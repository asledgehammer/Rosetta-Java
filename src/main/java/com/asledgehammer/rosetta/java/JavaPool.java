package com.asledgehammer.rosetta.java;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JavaPool {

  private final Map<String, JavaClass> classes = new HashMap<>();
  private final Map<String, JavaPackage> packages = new HashMap<>();

  public JavaPool() {}

  @NotNull
  public JavaPackage of(@NotNull Package pkg) {

    String name = pkg.getName();
    if (packages.containsKey(name)) {
      return packages.get(name);
    }

    // Create & cache the package definition.
    JavaPackage javaPackage = new JavaPackage(pkg);
    packages.put(name, javaPackage);
    return javaPackage;
  }

  @NotNull
  public JavaClass of(@NotNull Class<?> clazz) {
    String qualifiedPath = clazz.getName();
    if (classes.containsKey(qualifiedPath)) {
      return classes.get(qualifiedPath);
    }

    // Create & cache the class definition.
    JavaClass javaClass = new JavaClass(clazz);
    classes.put(qualifiedPath, javaClass);
    return javaClass;
  }

  @NotNull
  public JavaMethod of(@NotNull Method method) {
    Class<?> classDef = method.getDeclaringClass();
    JavaClass javaClass = of(classDef);
    return javaClass.getMethod(method);
  }
}
