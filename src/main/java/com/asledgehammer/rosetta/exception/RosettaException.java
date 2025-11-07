package com.asledgehammer.rosetta.exception;

import org.jetbrains.annotations.NotNull;

/**
 * RosettaException is a simple router exception to catch all Rosetta-specific runtime exceptions.
 */
public class RosettaException extends RuntimeException {

  /**
   * @param message The detail message. The detail message is saved for later retrieval by the
   *     getMessage() method.
   */
  public RosettaException(@NotNull String message) {
    super(message);
  }

  /**
   * @param message The detail message. The detail message is saved for later retrieval by the
   *     getMessage() method.
   * @param cause The cause (which is saved for later retrieval by the getCause() method). (A null
   *     value is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public RosettaException(@NotNull String message, @NotNull Throwable cause) {
    super(message, cause);
  }
}
