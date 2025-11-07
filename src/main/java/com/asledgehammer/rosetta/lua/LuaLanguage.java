package com.asledgehammer.rosetta.lua;

import com.asledgehammer.rosetta.RosettaLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LuaLanguage implements RosettaLanguage {

  @Override
  public void onLoad(@NotNull Map<String, Object> language) {}

  @NotNull
  @Override
  public Map<String, Object> onSave() {
    return Map.of();
  }

  @NotNull
  @Override
  public String getID() {
    return "lua";
  }
}
