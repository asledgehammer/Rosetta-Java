package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.NamedEntity;
import com.asledgehammer.rosetta.RosettaEntity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.regex.Pattern;

public class JavaParameter extends RosettaEntity implements NamedEntity {

  /** Used to validate and check names assigned to parameters. */
  private static final Pattern REGEX_PARAM_NAME = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");

  /** The formal name of the parameter. */
  private String name;

  public JavaParameter(@NotNull Parameter parameter) {
    super();

    // We already know that the name is valid.
    this.name = parameter.getName();
  }

  public JavaParameter(@NotNull Map<String, Object> raw) {
    super(raw);
    // NOTE: The name is provided as a property through `onLoad()`.
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {

    String name = (String) raw.get("name");
    setName(name);

    // TODO: Load type.
    // TODO: Load attributes / conditions?
  }

  @Override
  protected @NotNull Map<String, Object> onSave() {
    // TODO: Implement.
    return Map.of();
  }

  @NotNull
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Sets the formal name for the parameter.
   *
   * @param name The name to set.
   * @throws NullPointerException Thrown if the name is null.
   * @throws IllegalArgumentException Thrown if the name is empty.
   */
  public void setName(@NotNull String name) {
    if (name.isEmpty()) {
      throw new IllegalArgumentException("The name is empty.");
    } else if (!isValidName(name)) {
      throw new IllegalArgumentException("The name is not valid: " + name);
    }
    this.name = name;
  }

  /**
   * Tests if a Java-parameter's name is valid.
   *
   * @param name The name to test.
   * @return True if the parameter's name is valid.
   * @throws NullPointerException Thrown if the name is null.
   * @throws IllegalArgumentException Thrown if the name is empty.
   */
  public static boolean isValidName(@NotNull String name) {
    return !name.isEmpty() && REGEX_PARAM_NAME.matcher(name).find();
  }
}
