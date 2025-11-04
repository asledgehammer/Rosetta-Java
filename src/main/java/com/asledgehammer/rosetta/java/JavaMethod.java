package com.asledgehammer.rosetta.java;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Map;

public class JavaMethod extends JavaExecutable<Method> {

  private JavaReturn returns;

  JavaMethod(@NotNull Method method) {
    super(method);

    // TODO: Implement discovery.
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {}

  @Override
  protected @NotNull Map<String, Object> onSave() {
    return Map.of();
  }

  @Override
  public boolean onCompile() {
    // Invoke super-compile code and then handle return property.
    if (!super.onCompile()) {
      return false;
    }

    // Handle compiling the return property.
    if (returns.isDirty()) {
      return returns.compile();
    }

    return true;
  }

  @NotNull
  public JavaReturn getReturns() {
    return this.returns;
  }

  public void setReturns(@NotNull JavaReturn returns) {
    this.returns = returns;
    this.setDirty();
  }
}
