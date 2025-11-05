package com.asledgehammer.rosetta;

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

  public static Dump getYamlWriter() {
    return DEFAULT_DUMP;
  }

  public static Load getYamlReader() {
    return DEFAULT_LOAD;
  }
}
