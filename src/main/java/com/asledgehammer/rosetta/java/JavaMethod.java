package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.exception.ValueTypeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

public class JavaMethod extends JavaExecutable<Method> {

  @Nullable private JavaReturn returns;

  private final List<String> tags = new ArrayList<>();

  JavaMethod(@NotNull Method method) {
    super(method);

    // TODO: Implement discovery.
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {

    // Load all general executable data.
    super.onLoad(raw);

    // If return data is present, process it.
    if (raw.containsKey("return")) {
      Object oReturns = raw.get("return");
      if (!(oReturns instanceof Map)) {
        throw new ValueTypeException(name, "return", oReturns.getClass(), Map.class);
      }
      this.returns = new JavaReturn((Map<String, Object>) oReturns);
    }
  }

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

  @Nullable
  public JavaReturn getReturns() {
    return this.returns;
  }

  public void setReturns(@Nullable JavaReturn returns) {

    // Prevent redundant casts.
    if (this.returns == null) {
      if (returns == null) return;
    } else if (Objects.equals(returns, this.returns)) {
      return;
    }

    this.returns = returns;
    this.setDirty();
  }

  @Override
  public String toString() {
    return "JavaMethod \"" + getSignature() + "\"";
  }

  /**
   * @return True if one or more tags are applied.
   */
  public boolean hasTags() {
    return !this.tags.isEmpty();
  }

  /**
   * @return A read-only collection of applied tags.
   */
  @NotNull
  public List<String> getTags() {
    return Collections.unmodifiableList(tags);
  }

  /**
   * @param tag The tag to evaluate.
   * @return True if the tag is registered.
   * @throws NullPointerException If the tag is null.
   * @throws IllegalArgumentException If the tag is empty.
   */
  public boolean hasTag(@NotNull String tag) {
    if (tag.isEmpty()) {
      throw new IllegalArgumentException("The tag is empty.");
    }
    return this.tags.contains(tag);
  }

  /**
   * Applies a tag to the object.
   *
   * @param tag The tag to apply.
   * @throws NullPointerException If the tag is null.
   * @throws IllegalArgumentException If the tag is empty or already applied.
   */
  public void addTag(@NotNull String tag) {
    if (tag.isEmpty()) {
      throw new IllegalArgumentException("The tag is empty.");
    }
    if (tags.contains(tag)) {
      throw new IllegalArgumentException("The tag is already applied: " + tag);
    }
    this.tags.add(tag);
    setDirty();
  }

  /**
   * Removes a tag from the object.
   *
   * @param tag The tag to remove.
   * @throws NullPointerException If the tag is null.
   * @throws IllegalArgumentException If the tag is empty or is not applied.
   */
  public void removeTag(@NotNull String tag) {
    if (tag.isEmpty()) {
      throw new IllegalArgumentException("The tag is empty.");
    }
    if (!tags.contains(tag)) {
      throw new IllegalArgumentException("The tag is not applied: " + tag);
    }
    tags.remove(tag);
    setDirty();
  }

  /**
   * Clears all applied tags.
   *
   * @throws RuntimeException If the object has no tags. (Use {@link JavaField#hasTags()})
   */
  public void clearTags() {
    if (tags.isEmpty()) {
      throw new RuntimeException("No tags are registered.");
    }
    tags.clear();
  }
}
