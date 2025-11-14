package com.asledgehammer.rosetta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Notable {
  boolean hasNotes();

  @NotNull
  String getNotes();

  void setNotes(@Nullable String notes);
}
