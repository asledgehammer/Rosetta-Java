package com.asledgehammer.rosetta.exception;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ValueTypeException extends RosettaException {
  public ValueTypeException(
      @NotNull String dictionaryName,
      @NotNull String key,
      @NotNull Class<?> type,
      Class<?>... values) {
    super(
        "The value of "
            + dictionaryName
            + "[\""
            + key
            + "\"] is of type \""
            + type
            + "\". (Allowed Types: "
            + Arrays.toString(values));
  }
}
