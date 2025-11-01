package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.DirtySupported;
import com.asledgehammer.rosetta.NamedEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JavaExecutableList<E extends JavaExecutable> implements DirtySupported, NamedEntity {

  private final List<E> executables = new ArrayList<>();
  private boolean dirty;

  private final String name;

  public JavaExecutableList(@NotNull String name) {
    if (!JavaExecutable.isValidName(name)) {
      throw new IllegalArgumentException("The name is not a valid executable name. (Given: \"" + name + "\")");
    }
    this.name = name;
  }

  /**
   * @param executable The executable definition to test.
   * @return True if the executable definition is registered in the list.
   */
  public boolean hasExecutable(@NotNull E executable) {
    return executables.contains(executable);
  }

  /**
   * @param executable The executable definition to register to the list.
   */
  public void addExecutable(@NotNull E executable) {
    if (executables.contains(executable)) {
      throw new IllegalArgumentException(
          "The "
              + executable.getClass().getSimpleName()
              + " is already registered in the list: "
              + executable.getSignature());
    }
    this.setDirty();
  }

  /**
   * @param executable The executable definition to unregister from the list.
   */
  public void removeExecutable(@NotNull E executable) {
    if (!executables.contains(executable)) {
      throw new IllegalArgumentException(
          "The "
              + executable.getClass().getSimpleName()
              + " is NOT registered in the list: "
              + executable.getSignature());
    }
    executables.remove(executable);
    this.setDirty();
  }

  /** Sorts modified lists of executables to stack by signature strings. */
  public void sort() {
    // Only sort if the list is modified.
    if (this.isDirty()) {
      executables.sort(Comparator.comparing(JavaExecutable::getSignature));
      this.setDirty(false);
    }
  }

  /**
   * @return A read-only list of all executable members in the list.
   */
  @NotNull
  public List<E> getExecutables() {
    return Collections.unmodifiableList(this.executables);
  }

  @Override
  public boolean isDirty() {
    return this.dirty;
  }

  @Override
  public void setDirty(boolean flag) {
    this.dirty = flag;
  }

  @Override
  public String getName() {
    return this.name;
  }
}
