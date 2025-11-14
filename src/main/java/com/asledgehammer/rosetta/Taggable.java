package com.asledgehammer.rosetta;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Taggable {

  @NotNull
  List<String> getTags();

  void addTag(@NotNull String tag);

  default void addAllTags(@NotNull List<String> tags) {
    for (String tag : tags) {
      addTag(tag);
    }
  }

  boolean hasTags();

  boolean hasTag(@NotNull String tag);
}
