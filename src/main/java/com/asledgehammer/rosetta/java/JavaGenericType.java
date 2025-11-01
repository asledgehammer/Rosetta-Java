package com.asledgehammer.rosetta.java;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaGenericType extends JavaType {

  private final JavaType[] parameters;

  JavaGenericType(@NotNull String full) {
    super(extractBase(full));

    this.parameters = extractParameters(full);
  }

  public JavaGenericType(@NotNull Map<String, Object> raw) {
    super(extractBaseFromRaw(raw));

    this.parameters = extractParametersFromRaw(raw);
  }

  /**
   * @param type The fully-qualified type to parse.
   * @return The truncated type without generic parameter types.
   * @throws NullPointerException If the type is null.
   * @throws IllegalArgumentException If the type is an empty string.
   */
  private static String extractBase(@NotNull String type) {
    if (type.isEmpty()) {
      throw new IllegalArgumentException("The type is empty.");
    }
    int index = type.indexOf("<");
    if (index != -1) {
      return type.substring(type.indexOf("<"));
    }
    return type;
  }

  /**
   * @param raw The serialized data to parse.
   * @return The truncated type without generic parameter types.
   * @throws NullPointerException If the raw map is null.
   * @throws IllegalArgumentException If the raw map has no key "base".
   */
  private static String extractBaseFromRaw(@NotNull Map<String, Object> raw) {
    if (!raw.containsKey("base")) {
      throw new IllegalArgumentException("The raw map has no key \"base\".");
    }
    return (String) raw.get("base");
  }

  /**
   * @param type The fully-qualified type to parse.
   * @return A string-array where indices are fully-qualified generic parameter types.
   * @throws NullPointerException If the type is null.
   * @throws IllegalArgumentException If the type is empty.
   */
  private static JavaType[] extractParameters(@NotNull String type) {

    if (type.isEmpty()) {
      throw new IllegalArgumentException("The type is empty.");
    }

    String base = type.substring(type.indexOf("<"));
    List<String> parameters = new ArrayList<>();

    // .. TODO: Implement the parser.

    JavaType[] compiled = new JavaType[parameters.size()];
    for (int index = 0; index < parameters.size(); index++) {
      compiled[index] = JavaType.of(parameters.get(index));
    }
    return compiled;
  }

  private JavaType[] extractParametersFromRaw(@NotNull Map<String, Object> raw) {

    List<Object> rawParams = (List<Object>) raw.get("parameters");
    if (rawParams == null) {
      throw new IllegalArgumentException(
          "The raw properties doesn't contain a \"parameters\" list.");
    }

    int length = rawParams.size();
    JavaType[] compiled = new JavaType[length];
    for (int index = 0; index < length; index++) {
      compiled[index] = JavaType.of(rawParams.get(index).toString());
    }
    return compiled;
  }
}
