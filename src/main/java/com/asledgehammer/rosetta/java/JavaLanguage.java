package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.RosettaLanguage;
import com.asledgehammer.rosetta.exception.RosettaException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

public class JavaLanguage implements RosettaLanguage {

  final Map<String, JavaClass> classes = new HashMap<>();
  final Map<String, JavaPackage> packages = new HashMap<>();

  public JavaLanguage() {}

  @NotNull
  public JavaPackage of(@NotNull Package pkg) {

    String path = pkg.getName();
    if (packages.containsKey(path)) {
      return packages.get(path);
    }

    JavaPackage parent = null;
    if (path.contains(".")) {
      // Recursively resolve upward the parent & link to the base package object.
      String[] split = path.split("\\.");
      if (split.length == 2) {
        Package pkgParent = JavaPackage.resolve(split[0]);
        if (pkgParent != null) {
          parent = of(pkgParent);
        } else {
          parent = ofInternalPackage(split[0]);
        }
      } else {
        StringBuilder join = new StringBuilder(split[0]);
        for (int i = 1; i < split.length - 1; i++) {
          join.append(".").append(split[i]);
        }
        Package pkgParent = JavaPackage.resolve(join.toString());
        if (pkgParent != null) {
          parent = of(pkgParent);
        } else {
          parent = ofInternalPackage(join.toString());
        }
      }
    }

    // Create & cache the package definition.
    JavaPackage javaPackage = new JavaPackage(this, parent, pkg);
    packages.put(path, javaPackage);

    return javaPackage;
  }

  private JavaPackage ofInternalPackage(@NotNull String path) {

    JavaPackage parent = null;
    String name;
    if (path.contains(".")) {
      String[] split = path.split("\\.");
      name = split[split.length - 1];
      // TODO: Join

      if (split.length == 2) {
        Package pkgParent = JavaPackage.resolve(split[0]);
        if (pkgParent != null) {
          parent = of(pkgParent);
        } else {
          parent = ofInternalPackage(split[0]);
        }
      } else {
        StringBuilder join = new StringBuilder(split[0]);
        for (int i = 1; i < split.length - 1; i++) {
          join.append(".").append(split[i]);
        }
        Package pkgParent = JavaPackage.resolve(join.toString());
        if (pkgParent != null) {
          parent = of(pkgParent);
        } else {
          parent = ofInternalPackage(join.toString());
        }
      }
    } else {
      name = path;
    }

    return new JavaPackage(this, parent, name);
  }

  @NotNull
  public JavaClass of(@NotNull Class<?> clazz) {
    String qualifiedPath = clazz.getName();
    if (classes.containsKey(qualifiedPath)) {
      return classes.get(qualifiedPath);
    }

    // Create & cache the class definition.
    JavaPackage javaPackage = of(clazz.getPackage());
    JavaClass javaClass = new JavaClass(javaPackage, clazz);
    classes.put(qualifiedPath, javaClass);
    return javaClass;
  }

  @NotNull
  public JavaMethod of(@NotNull Method method) {
    Class<?> classDef = method.getDeclaringClass();
    JavaClass javaClass = of(classDef);
    return javaClass.getMethod(method);
  }

  @Override
  @SuppressWarnings({"unchecked"})
  public void onLoad(@NotNull Map<String, Object> java) {

    // No Java packages? Return.
    if (!java.containsKey("packages")) return;

    final Object oPackages = java.get("packages");
    if (!(oPackages instanceof Map)) {
      throw new RosettaException("The property \"languages.java.packages\" is not a dictionary.");
    }
    final Map<String, Object> packages = (Map<String, Object>) oPackages;

    final List<String> keys = new ArrayList<>(packages.keySet());
    keys.sort(Comparator.naturalOrder());
    for (String key : keys) {
      final Object oPackage = packages.get(key);
      if (!(oPackage instanceof Map)) {
        throw new RosettaException(
            "The property \"languages.java.packages." + key + "\" is not a dictionary.");
      }
      JavaPackage javaPackage = new JavaPackage(this, null, key, (Map<String, Object>) oPackage);
      packages.put(javaPackage.getName(), javaPackage);
    }
  }

  @Override
  public @NotNull Map<String, Object> onSave() {
    // TODO: Implement.
    return Map.of();
  }

  @NotNull
  @Override
  public String getID() {
    return "java";
  }
}
