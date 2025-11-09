package com.asledgehammer.rosetta.exception;

import org.jetbrains.annotations.NotNull;

public class MissingKeyException extends RosettaException {
  public MissingKeyException(@NotNull String dictionaryName, @NotNull String key) {
    super("The key is missing: " + dictionaryName + "[\"" + key + "\"]");
  }
}
