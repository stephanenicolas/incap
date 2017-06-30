package org.gradle.incap.impl.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.Element;

import static com.sun.tools.doclint.Entity.ge;
import static com.sun.tools.doclint.Entity.nu;

public class StateGraph {

  private Set<InputFile> inputFiles = new HashSet<>();
  private Set<ElementEntry> elementEntries= new HashSet<>();
  private  Set<GeneratedFile> generatedFiles = new HashSet<>();

  //forward edges
  private Map<InputFile, Set<ElementEntry>> mapInputToElements = new HashMap<>();
  private Map<ElementEntry, Set<GeneratedFile>> mapElementToGeneratedFiles = new HashMap<>();

  //backward edges
  private Map<GeneratedFile, Set<ElementEntry>> mapGeneratedFileToElements = new HashMap<>();
  private Map<ElementEntry, InputFile> mapElementToInputFiles = new HashMap<>();

  /**
   * The incremental filer will call this one.
   * @param name
   * @param originatingElements
   */
  public void addSourceFile(CharSequence name, Element... originatingElements) {
    GeneratedFile generatedFile = new GeneratedSourceFile(name);

    ElementEntry[] originatingElementEntries = new ElementEntry[originatingElements.length];
    for (int i = 0; i < originatingElementEntries.length; i++) {
      originatingElementEntries[i] = new ElementEntry(originatingElements[i]);
    }

    addGenerationEdge(generatedFile, originatingElementEntries);
  }

  /* package-private*/ void addGenerationEdge(GeneratedFile generatedFile, ElementEntry... originatingElements) {
    addBackwardEdge(generatedFile, originatingElements);
      addForwardEdge(generatedFile, originatingElements);
  }

    private void addForwardEdge(GeneratedFile generatedFile, ElementEntry... originatingElements) {
        for (ElementEntry originatingElement : originatingElements) {
            Set<GeneratedFile> elementEntries = mapElementToGeneratedFiles.get(originatingElement);
            if (elementEntries == null) {
                elementEntries = new HashSet<>();
            }
            elementEntries.add(generatedFile);
            mapElementToGeneratedFiles.put(originatingElement, elementEntries);
        }
    }

    private void addBackwardEdge(GeneratedFile generatedFile, ElementEntry... originatingElements) {
        Set<ElementEntry> originatingElementEntries = new HashSet<>();
        for (ElementEntry originatingElement : originatingElements) {
            originatingElementEntries.add(originatingElement);
        }

        mapGeneratedFileToElements.put(generatedFile, originatingElementEntries);
    }

  public Set<Element> getParticipatingElements(GeneratedFile generatedFile) {
    Set<ElementEntry> participatingElementEntries = getParticipatingElementEntries(generatedFile);
    Set<Element> elements = new HashSet<>();
    for (ElementEntry participatingElementEntry : participatingElementEntries) {
      elements.add(participatingElementEntry.getElement());
    }
    return elements;
  }

  public Set<ElementEntry> getParticipatingElementEntries(GeneratedFile generatedFile) {
    return mapGeneratedFileToElements.get(generatedFile);
  }
}
