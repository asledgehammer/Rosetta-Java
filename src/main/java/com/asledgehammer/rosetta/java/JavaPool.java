package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.Rosetta;
import com.asledgehammer.rosetta.RosettaException;
import org.jetbrains.annotations.NotNull;
import org.snakeyaml.engine.v2.api.Load;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.*;

public class JavaPool {

  final Map<String, JavaClass> classes = new HashMap<>();
  final Map<String, JavaPackage> packages = new HashMap<>();

  public JavaPool() {}

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
        if (pkgParent == null) {
          throw new RosettaException("Could not resolve parent package: " + split[0]);
        }
        parent = of(pkgParent);
      } else {
        StringBuilder join = new StringBuilder(split[0]);
        for (int i = 1; i < split.length - 1; i++) {
          join.append(".").append(split[i]);
        }
        Package pkgParent = JavaPackage.resolve(join.toString());
        if (pkgParent == null) {
          throw new RosettaException("Could not resolve parent package: " + join);
        }
        parent = of(pkgParent);
      }
    }

    // Create & cache the package definition.
    JavaPackage javaPackage = new JavaPackage(this, parent, pkg);
    packages.put(path, javaPackage);

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

  public void load(@NotNull File file) throws FileNotFoundException {
    Load reader = Rosetta.getYamlReader();
    Iterable<Object> oRaw = reader.loadAllFromReader(new FileReader(file));

    if (!(oRaw instanceof Map)) {
      throw new RuntimeException("Improperly formatted Rosetta YAML: " + file.getPath());
    }

    loadInternal((Map<String, Object>) oRaw);
  }

  public void load(@NotNull String yaml) {

    if (yaml.isEmpty()) {
      throw new IllegalArgumentException("The YAML string is empty.");
    }

    Load reader = Rosetta.getYamlReader();
    Iterable<Object> oRaw = reader.loadAllFromString(yaml);

    if (!(oRaw instanceof Map)) {
      throw new RuntimeException("Improperly formatted Rosetta YAML:\n" + yaml);
    }

    loadInternal((Map<String, Object>) oRaw);
  }

  @SuppressWarnings("unchecked")
  private void loadInternal(@NotNull Map<String, Object> raw) {

    // Grab the key.
    if (!raw.containsKey("version")) {
      throw new RosettaException("Missing \"version\" property at root of Rosetta YAML file.");
    }

    // If multi-version support in the future, convert to switch-table.
    final String version = raw.get("version").toString().trim();
    if (!version.equals("1.2")) {
      throw new RosettaException("Unknown version: " + version);
    }

    if (!raw.containsKey("languages")) {
      // No definitions? Return.
      return;
    }

    final Object oLanguages = raw.get("languages");
    if (!(oLanguages instanceof Map)) {
      throw new RosettaException("The property \"languages\" is not a dictionary.");
    }
    final Map<String, Object> languages = (Map<String, Object>) oLanguages;

    // No Java definitions? Return.
    if (!languages.containsKey("java")) return;

    final Object oJava = languages.get("java");
    if (!(oJava instanceof Map)) {
      throw new RosettaException("The property \"languages.java\" is not a dictionary.");
    }
    final Map<String, Object> java = (Map<String, Object>) oJava;

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
    }
  }

  private void loadPackage(String name, Map<String, Object> raw) {}

  @NotNull
  public Map<String, Object> save() {
    // TODO: Implement.
    return Map.of();
  }

  public void save(@NotNull File file) {
    // TODO: Implement.
  }
}
