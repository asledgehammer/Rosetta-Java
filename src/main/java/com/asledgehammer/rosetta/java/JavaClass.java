package com.asledgehammer.rosetta.java;

import com.asledgehammer.rosetta.NamedEntity;
import com.asledgehammer.rosetta.Reflected;
import com.asledgehammer.rosetta.RosettaEntity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;

public class JavaClass extends RosettaEntity implements NamedEntity, Reflected<Class<?>> {

  private final Map<String, JavaField> fields = new HashMap<>();
  private final Map<String, JavaExecutableList<JavaMethod>> methods = new HashMap<>();
  private final Map<String, JavaExecutableList<JavaConstructor>> constructors = new HashMap<>();
  private final List<JavaGenericParameter> parameters = new ArrayList<>();
  private final Class<?> reflectedObject;
  private final String name;

  JavaClass(@NotNull Class<?> clazz) {
    super();

    this.reflectedObject = clazz;
    this.name = clazz.getSimpleName();

    // TODO: Implement discovery.
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
      for (JavaExecutableList<JavaMethod> methodList : methods.values()) {
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
      for (JavaExecutableList<JavaConstructor> constructorList : constructors.values()) {
        for (JavaConstructor constructor : constructorList.getExecutables()) {
          if (constructor.isDirty()) {
            // Fail compilation if constructor fails to compile.
            if (!constructor.compile()) {
              return false;
            }
          }
        }
      }
    }

    return true;
  }

  @Override
  protected void onLoad(@NotNull Map<String, Object> raw) {}

  @NotNull
  @Override
  protected Map<String, Object> onSave() {
    return Map.of();
  }

  @Override
  public @NotNull String getName() {
    return name;
  }

  @NotNull
  public List<JavaGenericParameter> getParameters() {
    if (isDirty()) compile();
    return this.parameters;
  }

  @NotNull
  public Map<String, JavaExecutableList<JavaConstructor>> getConstructors() {
    if (isDirty()) compile();
    return this.constructors;
  }

  @NotNull
  public Map<String, JavaExecutableList<JavaMethod>> getMethods() {
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
    JavaExecutableList<JavaMethod> methods = this.methods.get(name);
    return methods.getExecutable(method);
  }

  @NotNull
  @Override
  public Class<?> getReflectedObject() {
    return this.reflectedObject;
  }
}
