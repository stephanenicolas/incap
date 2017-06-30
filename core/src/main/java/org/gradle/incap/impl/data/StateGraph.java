package org.gradle.incap.impl.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.Element;

public class StateGraph {

  private Set<InputFile> inputFiles = new HashSet<>();
  private Set<ElementEntry> elementEntries= new HashSet<>();
  private  Set<GeneratedFile> generatedFiles = new HashSet<>();

  //forward edges
  private Map<InputFile, Set<ElementEntry>> mapInputToElements = new HashMap<>();
  private Map<ElementEntry, Set<GeneratedFile>> mapElementToGeneratedFiles = new HashMap<>();

  //backward edges
  private Map<GeneratedFile, Set<ElementEntry>> mapGeneratedFileToElements = new HashMap<>();
  private Map<ElementEntry, Set<InputFile>> mapElementToInputFiles = new HashMap<>();

  /**
   * The incremental filer will call this one.
   * @param generatedFile
   * @param originatingElements
   */
  public void addSourceFile(CharSequence name, Element... originatingElements) {

  }

  /* package-private*/ void addGenerationEdge(GeneratedFile generatedFile, ElementEntry... originatingElements) {
    Set<ElementEntry> originatingElementEntries = new HashSet<>();
    for (Element originatingElement : originatingElements) {
      originatingElementEntries.add(new ElementEntry(originatingElement));
    }

    mapGeneratedFileToElements.put(generatedFile, originatingElementEntries);
  }

}
