package com.asledgehammer.rosetta.exception;

import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public class TypeException extends RosettaException {
  public TypeException(@NotNull String name, @NotNull Class<?> type, Class<?>... values) {
    super(
        "The value of \""
            + name
            + "\" is of type \""
            + type
            + "\". (Allowed Types: "
            + Arrays.toString(values));
  }
}
