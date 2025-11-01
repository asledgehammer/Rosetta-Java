package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.DirtySupported;
import com.asledgehammer.rosetta.NamedEntity;
import com.asledgehammer.rosetta.RosettaEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class JavaExecutable extends RosettaEntity implements DirtySupported, NamedEntity {

  /** Used to validate method names passed from serialized files. */
  private static final Pattern REGEX_EXECUTABLE_NAME = Pattern.compile("^[a-z][a-zA-Z0-9]*$");

  /** Used to prevent wasteful empty list instantiations in heap memory. */
  private static final List<JavaParameter> DEFAULT_EMPTY_LIST = List.of();

  private final List<JavaParameter> parameters = new ArrayList<>();
  private final String signature;
  private final String name;

  /** Defaults to true to compile first-time. */
  private boolean dirty = true;

  /**
   * Compiled when dirty to keep properties clean and forcefully runs through getter/setters for the
   * record.
   */
  private List<JavaParameter> parametersReadOnly = DEFAULT_EMPTY_LIST;

  /**
   * New Constructor for Method and Constructors.
   *
   * <p>NOTE: They must populate their properties in the sub-constructor calling this one.
   *
   * @param name The name of the executable.
   */
  protected JavaExecutable(@NotNull String name) {
    this.name = name;
    this.signature = createSignature(this);
  }

  /**
   * Load constructor for serialized data from files.
   *
   * @param name The name of the executable.
   * @param raw The data to load from a file.
   */
  public JavaExecutable(@NotNull String name, @NotNull Map<String, Object> raw) {
    super(raw);
    this.name = name;
    this.signature = createSignature(this);
  }

  @Override
  public boolean onCompile() {

    // Compile read-only list.
    if (this.hasParameters()) {

      // If any parameter is dirty, also compile.
      for (JavaParameter parameter : parameters) {
        if (parameter.isDirty()) {
          parameter.compile();
        }
      }

      this.parametersReadOnly = Collections.unmodifiableList(this.parameters);
    } else {
      this.parametersReadOnly = DEFAULT_EMPTY_LIST;
    }

    return true;
  }

  @Override
  public boolean isDirty() {

    // Normal dirty-flag check.
    if (this.dirty) return true;

    // Check parameter(s) for dirty flag.
    for (JavaParameter parameter : getParameters()) {
      if (parameter.isDirty()) {
        return true;
      }
    }

    // Methods will need to implement this by a super-call AND check return definition.

    return false;
  }

  @Override
  public void setDirty(boolean flag) {
    this.dirty = flag;
  }

  /**
   * @return True if the executable has no parameter definitions.
   */
  public boolean hasParameters() {
    return this.parameters.isEmpty();
  }

  /**
   * @return A read-only list of registered parameters for the executable definition.
   */
  @NotNull
  public List<JavaParameter> getParameters() {

    // Check to see if any parameter(s) were modified or added / removed to the executable. If so,
    // recompile to rebuild the list we return.
    if (isDirty()) {
      compile();
    }

    // Return the read-only list to prevent unintentional data-poisoning.
    return this.parametersReadOnly;
  }

  /**
   * @return The string-serialized signature.
   */
  @NotNull
  public String getSignature() {
    return this.signature;
  }

  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Serializes a java-executable definition as a signature string.
   *
   * @param executable The executable to serialize.
   * @return The serialized signature.
   * @throws NullPointerException If the executable is null.
   */
  public static String createSignature(@NotNull JavaExecutable executable) {
    // TODO: Implement.
    return "";
  }

  /**
   * Tests if a Java executable name is valid.
   *
   * @param name The name to test.
   * @return True if the executable's name is valid.
   * @throws NullPointerException If the name is null.
   */
  public static boolean isValidName(@NotNull String name) {
    return !name.isEmpty() && REGEX_EXECUTABLE_NAME.matcher(name).find();
  }
}
