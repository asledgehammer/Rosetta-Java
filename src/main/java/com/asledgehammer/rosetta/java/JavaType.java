package com.asledgehammer.rosetta.java;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JavaType {

  private static final Pattern REGEX_TYPE =
      Pattern.compile("^[a-zA-Z_$][a-zA-Z0-9_$]*(?:\\.[a-zA-Z_$][a-zA-Z0-9_$]*)*$");

  private static Map<String, JavaType> CACHE = new HashMap<>();

  private final String base;

  protected JavaType(@NotNull String base) {
    if (!isValidBaseType(base)) {
      throw new IllegalArgumentException("The base is not valid: \"" + base + "\"");
    }
    this.base = base;
  }

  /**
   * @return The base type string.
   */
  @NotNull
  public String getBase() {
    return this.base;
  }

  public static JavaType of(@NotNull String type) {
    return of(type, true);
  }

  @NotNull
  public static JavaType of(@NotNull String type, boolean useCache) {
    if (CACHE.containsKey(type)) {
      return CACHE.get(type);
    }

    JavaType obj;
    if (type.contains("<")) {
      obj = new JavaGenericType(type);
    } else {
      obj = new JavaType(type);
    }

    CACHE.put(type, obj);
    return obj;
  }

  /**
   * @param base The base type string.
   * @return True if the base string is valid. (Contains no illegal characters like generic types
   *     syntax)
   * @throws NullPointerException If the base is null.
   */
  private static boolean isValidBaseType(@NotNull String base) {
    return !base.isEmpty() && REGEX_TYPE.matcher(base).find();
  }

  public static void clearCache() {
    CACHE.clear();
  }
}
