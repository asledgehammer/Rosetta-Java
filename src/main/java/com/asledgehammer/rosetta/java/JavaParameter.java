package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.java.reference.TypeReference;
import com.asledgehammer.rosetta.NamedEntity;
import com.asledgehammer.rosetta.RosettaObject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.regex.Pattern;

public class JavaParameter extends RosettaObject implements NamedEntity, ReflectedReferenceable<Parameter> {

  /** Used to validate and check names assigned to parameters. */
  private static final Pattern REGEX_PARAM_NAME = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");

  private final Parameter reflectedObject;
  private TypeReference type;

  /** The formal name of the parameter. */
  private String name;

  public JavaParameter(@NotNull Parameter parameter) {
    super();

    this.reflectedObject = parameter;

    // We already know that the name is valid.
    this.name = parameter.getName();
    this.type = TypeReference.of(parameter.getParameterizedType());
  }

  @Override
  public boolean onCompile() {
    return true;
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {

    String name = (String) raw.get("name");
    setName(name);

    setType(TypeReference.of(raw.get("type").toString()));
    // TODO: Load attributes / conditions?
  }

  @NotNull
  @Override
  protected Map<String, Object> onSave() {
    // TODO: Implement.
    return Map.of();
  }

  @NotNull
  @Override
  public Parameter getReflectedObject() {
    return this.reflectedObject;
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

  @NotNull
  public TypeReference getType() {
    return this.type;
  }

  public void setType(@NotNull TypeReference type) {
    this.type = type;
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
