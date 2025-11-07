package com.asledgehammer.rosetta.exception;

import org.jetbrains.annotations.NotNull;

public class UnsupportedApplicationException extends RosettaException {
  public UnsupportedApplicationException(@NotNull String id) {
    super("Unsupported or unloaded RosettaApplication: " + id);
  }
}
