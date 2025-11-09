package com.asledgehammer.rosetta.java;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaConstructor extends JavaExecutable<Constructor<?>> {

  JavaConstructor(@NotNull Constructor<?> constructor) {
    super(constructor);
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {}

  @NotNull
  @Override
  protected Map<String, Object> onSave() {
    return Map.of();
  }
}
