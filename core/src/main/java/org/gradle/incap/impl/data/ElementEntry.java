package org.gradle.incap.impl.data;

import javax.lang.model.element.Element;

public class ElementEntry {

  private Element element;
  private String eqlPath;

  public ElementEntry(Element element) {
    this.eqlPath = generateEqlPath(element);
    this.element = element;
  }

  public ElementEntry(String eqlPath) {
    this.eqlPath = eqlPath;
  }

  public Element getElement() {
    if (element == null) {
      element = lookupElement(eqlPath);
    }
    return element;
  }

  private String generateEqlPath(Element element) {
    return null;
  }

  private Element lookupElement(String eqlPath) {
    return null;
  }
}
