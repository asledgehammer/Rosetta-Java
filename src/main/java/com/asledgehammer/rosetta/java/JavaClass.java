package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.NamedEntity;
import com.asledgehammer.rosetta.Notable;
import com.asledgehammer.rosetta.RosettaObject;
import com.asledgehammer.rosetta.Taggable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class JavaClass extends RosettaObject
    implements NamedEntity, Notable, Reflected<Class<?>>, Taggable {

  private final Map<String, JavaField> fields = new HashMap<>();
  private final Map<String, JavaExecutableCollection<JavaMethod>> methods = new HashMap<>();
  private final List<JavaGenericParameter> parameters = new ArrayList<>();
  private final List<String> tags = new ArrayList<>();
  private final JavaExecutableCollection<JavaConstructor> constructors;
  private Class<?> target;
  private final JavaPackage pkg;
  private String notes;
  private final String name;

  JavaClass(@NotNull JavaPackage pkg, @NotNull Class<?> clazz) {
    super();

    this.pkg = pkg;
    this.name = clazz.getSimpleName();
    this.constructors = new JavaExecutableCollection<>(this.name);

    this.target = clazz;

    discover();
  }

  JavaClass(@NotNull JavaPackage pkg, @NotNull String name, @NotNull Map<String, Object> raw) {
    super();

    this.pkg = pkg;
    this.name = name;
    this.constructors = new JavaExecutableCollection<>(this.name);

    // Attempt to resolve reflection before loading.
    this.target = resolve(pkg.getPath() + "." + name);

    onLoad(raw);
  }

  private void discover() {
    Class<?> clazz = this.target;

    // Discover fields.
    for (Field field : clazz.getDeclaredFields()) {
      JavaField javaField = new JavaField(field);
      fields.put(javaField.getName(), javaField);
    }

    // Discover constructors.
    for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
      JavaConstructor javaConstructor = new JavaConstructor(constructor);
      constructors.addExecutable(javaConstructor);
    }

    // Discover methods.
    for (Method method : clazz.getDeclaredMethods()) {
      String name = method.getName();
      JavaMethod javaMethod = new JavaMethod(method);
      JavaExecutableCollection<JavaMethod> collection = methods.get(name);
      if (collection == null) {
        collection = new JavaExecutableCollection<>(name);
        methods.put(name, collection);
      }
      collection.addExecutable(javaMethod);
    }
  }

  @Override
  public boolean onCompile() {

    // Compile field(s).
    if (!fields.isEmpty()) {
      for (JavaField field : fields.values()) {
        if (field.isDirty()) {
          // Fail compilation if field fails to compile.
          if (!field.compile()) {
            return false;
          }
        }
      }
    }

    // Compile method(s).
    if (!methods.isEmpty()) {
      for (JavaExecutableCollection<JavaMethod> methodList : methods.values()) {
        for (JavaMethod method : methodList.getExecutables()) {
          if (method.isDirty()) {
            // Fail compilation if method fails to compile.
            if (!method.compile()) {
              return false;
            }
          }
        }
      }
    }

    // Compile constructor(s).
    if (!constructors.isEmpty()) {
      for (JavaConstructor constructor : constructors.getExecutables()) {
        if (constructor.isDirty()) {
          // Fail compilation if constructor fails to compile.
          if (!constructor.compile()) {
            return false;
          }
        }
      }
    }

    return true;
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {
    // TODO: Implement.
  }

  @NotNull
  @Override
  protected Map<String, Object> onSave() {
    return Map.of();
  }

  @Override
  public String toString() {
    return "JavaClass \"" + getPackage().getPath() + "." + getName() + "\"";
  }

  @NotNull
  @Override
  public String getName() {
    return name;
  }

  @NotNull
  public List<JavaGenericParameter> getParameters() {
    if (isDirty()) compile();
    return this.parameters;
  }

  @NotNull
  public JavaExecutableCollection<JavaConstructor> getConstructors() {
    if (isDirty()) compile();
    return this.constructors;
  }

  @NotNull
  public Map<String, JavaExecutableCollection<JavaMethod>> getMethods() {
    if (isDirty()) compile();
    return this.methods;
  }

  @NotNull
  public Map<String, JavaField> getFields() {
    if (isDirty()) compile();
    return this.fields;
  }

  @NotNull
  public JavaMethod getMethod(@NotNull Method method) {
    String name = method.getName();
    JavaExecutableCollection<JavaMethod> methods = this.methods.get(name);
    return methods.getExecutable(method);
  }

  @NotNull
  @Override
  public Class<?> getReflectionTarget() {
    return this.target;
  }

  void setReflectedObject(@Nullable Class<?> target) {
    this.target = target;
  }

  @NotNull
  public JavaPackage getPackage() {
    return pkg;
  }

  @Override
  public boolean hasNotes() {
    return this.notes != null && !this.notes.isEmpty();
  }

  @Override
  @NotNull
  public String getNotes() {
    if (!hasNotes()) {
      throw new NullPointerException("The object has no notes.");
    }
    return this.notes;
  }

  @Override
  public void setNotes(@Nullable String notes) {
    notes = notes == null || notes.isEmpty() ? null : notes;

    // Catch redundant changes to not set dirty flag.
    if (this.notes == null) {
      if (notes == null) return;
    } else if (this.notes.equals(notes)) return;

    this.notes = notes;

    setDirty();
  }

  @Override
  public boolean hasTags() {
    return !this.tags.isEmpty();
  }

  @NotNull
  @Override
  public List<String> getTags() {
    return Collections.unmodifiableList(tags);
  }

  @Override
  public boolean hasTag(@NotNull String tag) {
    if (tag.isEmpty()) {
      throw new IllegalArgumentException("The tag is empty.");
    }
    return this.tags.contains(tag);
  }

  @Override
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

  @Override
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

  @NotNull
  @Override
  public List<String> clearTags() {
    if (!hasTags()) {
      throw new RuntimeException("No tags are registered.");
    }
    List<String> tagsRemoved = Collections.unmodifiableList(tags);
    tags.clear();
    return tagsRemoved;
  }

  @Nullable
  public static Class<?> resolve(@NotNull String path) {
    return resolve(path, ClassLoader.getSystemClassLoader());
  }

  @Nullable
  public static Class<?> resolve(@NotNull String path, @NotNull ClassLoader classLoader) {
    try {
      return Class.forName(path, false, classLoader);
    } catch (Exception e) {
      return null;
    }
  }
}
