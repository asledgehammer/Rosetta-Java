package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.NamedEntity;
import com.asledgehammer.rosetta.RosettaEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class JavaClass extends RosettaEntity implements NamedEntity {

  private String name;

  private final Map<String, JavaField> fields = new HashMap<>();
  private final Map<String, JavaExecutableList<JavaMethod>> methods = new HashMap<>();
  private final Map<String, JavaExecutableList<JavaConstructor>> constructors = new HashMap<>();

  public JavaClass(@NotNull Map<String, Object> raw) {
    super(raw);
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {}

  @NotNull
  @Override
  protected Map<String, Object> onSave() {
    return Map.of();
  }

  @Override
  public String getName() {
    return name;
  }
}
