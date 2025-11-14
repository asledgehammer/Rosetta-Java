package com.asledgehammer.rosetta;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * RosettaObject is a common super-class for dictionary objects that monitors its dirty-state for
 * compiling and modifying its properties.
 */
public abstract class RosettaObject implements DirtySupported {

  private boolean dirty = false;

  /** Generic creation constructor. No arguments are passed. */
  protected RosettaObject() {}

  /**
   * Load constructor. Passes a serialized map of data, often loaded from a serialized file.
   *
   * @param raw The raw data to load through {@link RosettaObject#onLoad(Map)}
   * @throws NullPointerException If the raw map is null.
   */
  protected RosettaObject(@NotNull Map<String, Object> raw) {
    onLoad(raw);
  }

  @Override
  public boolean onCompile() {
    return true;
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  @Override
  public void setDirty() {
    this.dirty = true;
  }

  public void setDirty(boolean flag) {
    this.dirty = flag;
  }

  /**
   * Handles loading the Rosetta object.
   *
   * @param raw The raw data to load.
   * @throws NullPointerException If the raw map is null.
   */
  protected abstract void onLoad(@NotNull Map<String, Object> raw);

//  /**
//   * @return The serialized map of the object.
//   */
//  @NotNull
//  protected abstract Map<String, Object> onSave();
}
