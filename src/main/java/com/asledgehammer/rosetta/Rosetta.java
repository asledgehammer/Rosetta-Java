package com.asledgehammer.rosetta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.common.FlowStyle;

public class Rosetta {

  private static final Load DEFAULT_LOAD;
  private static final Dump DEFAULT_DUMP;

  static {
    LoadSettings loadSettings = LoadSettings.builder().build();
    DEFAULT_LOAD = new Load(loadSettings);
    DumpSettings dumpSettings = DumpSettings.builder().setDefaultFlowStyle(FlowStyle.BLOCK).build();
    DEFAULT_DUMP = new Dump(dumpSettings);
  }

  private final Load load;
  private final Dump dump;

  private Rosetta(@Nullable Load load, @Nullable Dump dump) {
    if (load == null) {
      load = DEFAULT_LOAD;
    }
    this.load = load;
    if (dump == null) {
      dump = DEFAULT_DUMP;
    }
    this.dump = dump;
  }

  @NotNull
  public Load getLoad() {
    return this.load;
  }

  @NotNull
  public Dump getDump() {
    return this.dump;
  }
}
