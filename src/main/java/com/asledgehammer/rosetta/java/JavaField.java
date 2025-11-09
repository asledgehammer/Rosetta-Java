package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.NamedEntity;
import com.asledgehammer.rosetta.exception.MissingKeyException;
import com.asledgehammer.rosetta.java.reference.TypeReference;
import com.asledgehammer.rosetta.RosettaObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public class JavaField extends RosettaObject implements NamedEntity, ReflectedReferenceable<Field> {

  private final Field reflectedObject;
  private final String name;

  @Nullable private String notes;
  @Nullable private String deprecated;

  private TypeReference type;

  private final List<String> tags = new ArrayList<>();

  JavaField(@NotNull Field field) {
    super();

    this.name = field.getName();
    this.reflectedObject = field;
    this.type = TypeReference.of(field.getGenericType());
  }

  JavaField(@NotNull String name, @NotNull Map<String, Object> raw) {
    super();

    this.name = name;
    this.reflectedObject = null;

    onLoad(raw);
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {
    if (!raw.containsKey("type")) {
      throw new MissingKeyException(name, "type");
    }
    this.type = JavaLanguage.resolveType(raw.get("type"));
  }

  @NotNull
  @Override
  protected Map<String, Object> onSave() {
    return Map.of();
  }

  @NotNull
  public TypeReference getType() {
    return this.type;
  }

  public void setType(@NotNull TypeReference type) {
    this.type = type;
  }

  @Nullable
  @Override
  public Field getReflectedObject() {
    return this.reflectedObject;
  }

  @NotNull
  @Override
  public String getName() {
    return this.name;
  }

  @Nullable
  public String getNotes() {
    return this.notes;
  }

  public void setNotes(@Nullable String notes) {
    notes = notes == null || notes.isEmpty() ? null : notes;

    // Catch redundant changes to not set dirty flag.
    if (this.notes == null) {
      if (notes == null) return;
    } else if (this.notes.equals(notes)) return;

    this.notes = notes;

    setDirty();
  }

  /**
   * @return True if the field is flagged as deprecated.
   */
  public boolean isDeprecated() {
    return this.deprecated != null;
  }

  /**
   * @return The deprecation message. If empty, the deprecation flag is set and no message is
   *     provided.
   * @throws NullPointerException If no deprecated message is set. (Use {@link
   *     JavaField#isDeprecated()} to check before invoking this method)
   */
  @NotNull
  public String getDeprecatedMessage() {
    if (this.deprecated == null) {
      throw new NullPointerException("The field is not deprecated. (No message set)");
    }
    return this.deprecated;
  }

  /**
   * Sets the deprecation flag of the field without a message.
   *
   * @param flag The flag to set.
   */
  public void setDeprecated(boolean flag) {
    String deprecated = flag ? "" : null;
    if (Objects.equals(this.deprecated, deprecated)) {
      return;
    }
    this.deprecated = deprecated;
    setDirty();
  }

  /**
   * @param message The message to set. If empty, the deprecation flag is set to true, but no
   *     message is provided. If null, the deprecation flag is set to false.
   */
  public void setDeprecated(@Nullable String message) {
    if (Objects.equals(this.deprecated, message)) {
      return;
    }
    this.deprecated = message;
    this.setDirty();
  }

  @Override
  public String toString() {
    return "JavaField \"" + getName() + "\"";
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
