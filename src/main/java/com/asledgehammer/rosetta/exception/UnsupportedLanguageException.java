package com.asledgehammer.rosetta.exception;

import org.jetbrains.annotations.NotNull;

public class UnsupportedLanguageException extends RuntimeException {
  public UnsupportedLanguageException(@NotNull String id) {
    super("Unsupported or unloaded RosettaLanguage: " + id);
  }
}
