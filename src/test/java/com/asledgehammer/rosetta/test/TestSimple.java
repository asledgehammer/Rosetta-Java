package com.asledgehammer.rosetta.test;

import com.asledgehammer.rosetta.Rosetta;
import com.asledgehammer.rosetta.RosettaCollection;
import com.asledgehammer.rosetta.java.JavaClass;
import com.asledgehammer.rosetta.java.JavaLanguage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestSimple {

  @Test
  public void test() {

    JavaLanguage language = new JavaLanguage();
    JavaClass javaClass = language.of(ArrayList.class);

    System.out.println(javaClass);

    RosettaCollection collection = Rosetta.createCollection();
    collection.addLanguage(language);
  }
}
