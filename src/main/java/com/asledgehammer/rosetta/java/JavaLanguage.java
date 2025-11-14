package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.RosettaLanguage;
import com.asledgehammer.rosetta.exception.MissingKeyException;
import com.asledgehammer.rosetta.exception.RosettaException;
import com.asledgehammer.rosetta.exception.TypeException;
import com.asledgehammer.rosetta.exception.ValueTypeException;
import com.asledgehammer.rosetta.java.reference.SimpleTypeReference;
import com.asledgehammer.rosetta.java.reference.TypeReference;
import com.asledgehammer.rosetta.java.reference.UnionTypeReference;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

public class JavaLanguage implements RosettaLanguage {

  final Map<String, JavaClass> classes = new HashMap<>();
  final Map<String, JavaPackage> packages = new HashMap<>();

  public JavaLanguage() {}

  /**
   * Resolves a TypeReference from Rosetta-defined data.
   *
   * @param oType Either a String or a Map.
   * @return A built type-reference.
   */
  public static TypeReference resolveType(@NotNull Object oType) {

    if (oType instanceof String) {
      return TypeReference.of((String) oType);
    } else if (!(oType instanceof Map)) {
      throw new TypeException("type", oType.getClass(), String.class, Map.class);
    }

    Map<String, Object> map = (Map<String, Object>) oType;

    // Retrieve the base string.
    Object oBase = map.get("base");
    if (oBase == null) {
      throw new MissingKeyException("type", "base");
    } else if (!(oBase instanceof String)) {
      throw new ValueTypeException("type", "base", oBase.getClass(), String.class);
    }

    String full = (String) oBase;
    // Retrieve any parameters defined.
    if (map.containsKey("parameters")) {
      Object oParameters = map.get("parameters");
      if (!(oParameters instanceof List)) {
        throw new ValueTypeException("type", "parameters", oParameters.getClass(), List.class);
      }

      StringBuilder sub = new StringBuilder();
      for (Object oParameter : (List) oParameters) {
        if (sub.isEmpty()) {
          sub.append(resolveType(oParameter).getBase());
        } else {
          sub.append(", ").append(resolveType(oParameter).getBase());
        }
      }
      full += "<" + sub + ">";
    }

    return TypeReference.of(full);
  }

  /**
   * @param type The type to serialize.
   * @return Either a {@link String} for {@link SimpleTypeReference without {@link
   *     SimpleTypeReference#hasSubTypes()} being true} or a {@link Map}.
   */
  @NotNull
  public static Object serializeType(@NotNull TypeReference type) {
    Map<String, Object> raw;
    if (type instanceof SimpleTypeReference simple) {
      if (!simple.hasSubTypes()) {
        return simple.compile();
      }
      raw = new HashMap<>();
      raw.put("full", simple.compile());
      raw.put("base", simple.getBase());
      List<Object> parameters = new ArrayList<>();
      for (TypeReference subType : simple.getSubTypes()) {
        parameters.add(serializeType(subType));
      }
      raw.put("parameters", parameters);
    } else {
      UnionTypeReference union = (UnionTypeReference) type;
      raw = new HashMap<>();
      raw.put("full", union.compile());
      raw.put("base", union.getBase());
      raw.put("generic", union.isGeneric());
      raw.put("bounds_type", union.isExtendsOrSuper() ? "extends" : "super");
      List<Object> bounds = new ArrayList<>();
      for (TypeReference bound : union.getBounds()) {
        bounds.add(serializeType(bound));
      }
      raw.put("bounds", bounds);
    }
    return raw;
  }

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
