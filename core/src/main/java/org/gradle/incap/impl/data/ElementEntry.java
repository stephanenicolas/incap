package org.gradle.incap.impl.data;

import java.util.HashSet;
import java.util.Set;
import javax.lang.model.element.Element;

public class ElementEntry {

  private Element element;
  private Set<GeneratedFile> generatedFiles = new HashSet<>();
  private String eqlPath;

  public ElementEntry(Element element) {
    this.eqlPath = generateEqlPath(element);
  }

  private String generateEqlPath(Element element) {
    return null;
  }
}
