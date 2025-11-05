package com.asledgehammer.rosetta;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class RosettaObject implements DirtySupported {

  private boolean dirty = false;

  protected RosettaObject() {}

  protected RosettaObject(@NotNull Map<String, Object> raw) {
    onLoad(raw);
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

  protected abstract void onLoad(@NotNull Map<String, Object> raw);

  @NotNull
  protected abstract Map<String, Object> onSave();
}
