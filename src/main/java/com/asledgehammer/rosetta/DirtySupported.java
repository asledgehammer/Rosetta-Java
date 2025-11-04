package com.asledgehammer.rosetta;

/**
 * DirtySupported objects store a dirty flag to handle updates to properties and affected properties
 * needing compiling.
 */
public interface DirtySupported {

  /**
   * Compute and update the object. (If dirty)
   *
   * @return True if the object is considered not-dirty. False if the compilation fails or is not
   *     considered compiled and is dirty.
   */
  default boolean compile() {
    if (!isDirty()) return true;
    try {
      if (onCompile()) {
        setDirty(false);
        return true;
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to compile dirty " + getClass().getSimpleName() + ".", e);
    }
    return false;
  }

  /**
   * @return True if the object is dirty and needs compiling.
   */
  boolean isDirty();

  /**
   * Sets the object's dirty state.
   *
   * @param flag The flag to set.
   */
  void setDirty(boolean flag);

  /** Sets the object dirty. */
  default void setDirty() {
    setDirty(true);
  }

  /**
   * Handles compiling dirty objects. Only objects identifying as dirty will compile.
   *
   * @return True if the object has compiled and can set itself as clean.
   */
  boolean onCompile();
}
