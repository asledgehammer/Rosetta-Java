package com.asledgehammer.rosetta.test;

import com.asledgehammer.rosetta.java.reference.SimpleTypeReference;
import com.asledgehammer.rosetta.java.reference.TypeReference;
import org.junit.jupiter.api.Test;

public class TestTypeReferenceString {

  @Test
  public void test() {
    String type = "java.util.ArrayList<java.lang.String>";

    TypeReference typeReference = TypeReference.of(type);
    assert typeReference.getBase().equals("java.util.ArrayList");
    assert typeReference instanceof SimpleTypeReference;
    assert ((SimpleTypeReference) typeReference).hasSubTypes();
    assert ((SimpleTypeReference) typeReference).getSubTypes().size() == 1;
    assert ((SimpleTypeReference) typeReference)
        .getSubTypes()
        .get(0)
        .getBase()
        .equals("java.lang.String");

    System.out.println(typeReference.getBase());
    System.out.println(((SimpleTypeReference) typeReference).getSubTypes());
  }
}
