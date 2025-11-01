package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.DirtySupported;
import com.asledgehammer.rosetta.NamedEntity;
import com.asledgehammer.rosetta.RosettaEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JavaPackage extends RosettaEntity implements DirtySupported, NamedEntity {

  /** To test Java package names for validity. */
  private static final Pattern REGEX_PKG_NAME =
      Pattern.compile("^[a-z_][a-z0-9_]*$", Pattern.CASE_INSENSITIVE);

  /** Stores class definitions in the package. */
  private final Map<String, JavaClass> classes = new HashMap<>();

  /** Stores sub-package definitions. */
  private final Map<String, JavaPackage> packages = new HashMap<>();

  /** The package name. */
  private final String name;

  /**
   * Creation constructor for new package definitions.
   *
   * @param pkg The Java-Reflection package instance.
   */
  public JavaPackage(@NotNull Package pkg) {
    super();

    // We already know that this is a valid package-name.
    this.name = pkg.getName();
  }

  /**
   * Load constructor for a serialized package definition.
   *
   * @param name The package-name.
   * @param raw The raw-data loaded from a file.
   */
  public JavaPackage(@NotNull String name, @NotNull Map<String, Object> raw) {
    super(raw);

    // Make sure the name is a valid Java package name.
    if (!isValidName(name)) {
      throw new IllegalArgumentException("Invalid package name: " + name);
    }

    this.name = name;
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {
    // TODO: Load classes.
    // TODO: Load sub-packages.
  }

  @NotNull
  @Override
  protected Map<String, Object> onSave() {
    return Map.of();
  }

  /**
   * @return Returns a read-only map of the classes in the package.
   */
  @NotNull
  public Map<String, JavaClass> getClasses() {
    return Collections.unmodifiableMap(classes);
  }

  /**
   * @return Returns a read-only map of the sub-packages in the package.
   */
  @NotNull
  public Map<String, JavaPackage> getPackages() {
    return Collections.unmodifiableMap(packages);
  }

  /**
   * @param pkg The sub-package definition to register.
   * @throws IllegalArgumentException If the sub-package definition is already registered.
   */
  public void addPackage(@NotNull JavaPackage pkg) {
    String pkgName = pkg.getName();
    if (this.packages.containsKey(pkgName)) {
      throw new IllegalArgumentException(
          "The package \"" + this.name + "\" already contains sub-package: \"" + pkgName + "\"");
    }
    this.packages.put(pkg.getName(), pkg);
  }

  /**
   * @param pkg The sub-package definition to unregister.
   * @throws IllegalArgumentException If the sub-package definition is NOT registered.
   */
  public void removePackage(@NotNull JavaPackage pkg) {
    String pkgName = pkg.getName();
    if (!this.packages.containsKey(pkgName)) {
      throw new IllegalArgumentException(
          "The package \"" + this.name + "\" doesn't contain sub-package: \"" + pkgName + "\"");
    }
    this.packages.remove(pkgName);
  }

  /**
   * @param pkgName The name of the sub-package definition to unregister. (Case-Sensitive)
   * @return The unregistered sub-package definition.
   * @throws IllegalArgumentException If the sub-package definition is NOT registered.
   */
  public JavaPackage removePackage(@NotNull String pkgName) {
    if (!this.packages.containsKey(pkgName)) {
      throw new IllegalArgumentException(
          "The package \"" + this.name + "\" doesn't contain sub-package: \"" + pkgName + "\"");
    }
    return this.packages.remove(pkgName);
  }

  /**
   * @param pkg The sub-package definition.
   * @return True if the sub-package definition exists AND is registered.
   */
  public boolean hasPackage(@NotNull JavaPackage pkg) {
    return this.packages.containsKey(pkg.getName());
  }

  /**
   * @param pkgName The name of the package. (Case-Sensitive)
   * @return True if the sub-package definition exists AND is registered.
   */
  public boolean hasPackage(@NotNull String pkgName) {
    return this.packages.containsKey(pkgName);
  }

  /**
   * @param pkgName The name of the package. (Case-Sensitive)
   * @return The sub-package definition.
   * @throws NullPointerException If the sub-package definition doesn't exist.
   */
  @NotNull
  public JavaPackage getPackage(@NotNull String pkgName) {
    if (!this.packages.containsKey(pkgName)) {
      throw new NullPointerException(
          "Package \"" + this.name + "\" doesn't have the sub-package: \"" + pkgName + "\"");
    }
    return this.packages.get(pkgName);
  }

  /**
   * @param clazz The class definition.
   * @return True if the class definition exists AND is registered.
   */
  public boolean hasClazz(@NotNull JavaClass clazz) {
    return this.classes.containsKey(clazz.getName());
  }

  /**
   * @param clazzName The name of the class definition.
   * @return True if a class definition with the name exists AND is registered.
   */
  public boolean hasClazz(@NotNull String clazzName) {
    return this.classes.containsKey(clazzName);
  }

  /**
   * @param clazzName The name of the class. (Case-Sensitive)
   * @return The class definition.
   * @throws NullPointerException If the class definition doesn't exist.
   */
  @NotNull
  public JavaClass getClazz(@NotNull String clazzName) {
    if (!this.classes.containsKey(clazzName)) {
      throw new NullPointerException(
          "Package \"" + this.name + "\" doesn't have the class: \"" + clazzName + "\"");
    }
    return this.classes.get(clazzName);
  }

  /**
   * @param clazz The class definition to register.
   * @throws IllegalArgumentException If the class definition is already registered.
   */
  public void addClass(@NotNull JavaClass clazz) {
    String clazzName = clazz.getName();
    if (this.classes.containsKey(clazzName)) {
      throw new IllegalArgumentException(
              "The package \"" + this.name + "\" already contains class: \"" + clazzName + "\"");
    }
    this.classes.put(clazzName, clazz);
  }

  /**
   * @param clazz The class definition to unregister.
   * @throws IllegalArgumentException If the class definition is NOT registered.
   */
  public void removeClazz(@NotNull JavaClass clazz) {
    String clazzName = clazz.getName();
    if (!this.classes.containsKey(clazzName)) {
      throw new IllegalArgumentException(
              "The package \"" + this.name + "\" doesn't contain class: \"" + clazzName + "\"");
    }
    this.classes.remove(clazzName);
  }

  /**
   * @param clazzName The name of the class definition to unregister. (Case-Sensitive)
   * @return The unregistered class definition.
   * @throws IllegalArgumentException If the class definition is NOT registered.
   */
  public JavaClass removeClazz(@NotNull String clazzName) {
    if (!this.classes.containsKey(clazzName)) {
      throw new IllegalArgumentException(
              "The package \"" + this.name + "\" doesn't contain class: \"" + clazzName + "\"");
    }
    return this.classes.remove(clazzName);
  }

  @NotNull
  @Override
  public String getName() {
    return name;
  }

  /**
   * Tests if a Java-package name is valid.
   *
   * @param name The name to test.
   * @return True if the package name is valid.
   */
  public static boolean isValidName(@NotNull String name) {
    return !name.isEmpty() && REGEX_PKG_NAME.matcher(name).find();
  }
}
