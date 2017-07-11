package org.gradle.incap.impl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.gradle.incap.impl.data.ElementEntry;
import org.gradle.incap.impl.data.GeneratedFile;
import org.gradle.incap.impl.data.GeneratedSourceFile;
import org.gradle.incap.impl.data.InputFile;
import org.gradle.incap.impl.data.StateGraph;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.gradle.incap.impl.utils.StateGraphMatcher.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class StateGraphUtilsTest {
  private StateGraphUtils stateGraphUtils = new StateGraphUtils();

  @Rule public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void testSaveReadStateGraph() throws IOException, ClassNotFoundException {
    File stateGraphFile = folder.newFile("stateGraph.ser");
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(stateGraphFile));
    StateGraph stateGraph = createStageGraph();
    stateGraphUtils.saveToStream(objectOutputStream, stateGraph);
    objectOutputStream.close();

    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(stateGraphFile));
    StateGraph stateGraphFromFile = stateGraphUtils.readFromStream(objectInputStream);

    assertEquals(stateGraph, stateGraphFromFile);
  }

  @Test
  public void testImportStateGraphToM1Format() throws IOException {
    //GIVEN
    File stateGraphFile = folder.newFile("stateGraph.txt");
    PrintWriter writer = new PrintWriter(new FileOutputStream(stateGraphFile));
    StateGraph stateGraph = createStageGraph();

    //WHEN
    stateGraphUtils.exportToM1Format(writer, stateGraph);
    writer.close();

    //THEN
    BufferedReader reader = new BufferedReader(new FileReader(stateGraphFile));
    String line = reader.readLine();
    assertThat(line, is("1"));
    line = reader.readLine();
    assertThat(line, is("SOURCE:GeneratedFile.java <-- [org.gradle.incap.example.InputClass]"));
    line = reader.readLine();
    assertThat(line, nullValue());
    reader.close();
  }

  private StateGraph createStageGraph() {
    StateGraph stateGraph = new StateGraph(null, null);

    InputFile testInputFile = new InputFile("org.gradle.incap.example.InputClass");

    Set<ElementEntry> elementEntries = new HashSet<>();
    elementEntries.add(new ElementEntry(null, "element entry"));

    Set<GeneratedFile> generatedFiles = new HashSet<>();
    generatedFiles.add(new GeneratedSourceFile("GeneratedFile.java"));

    // forward edge
    Map<InputFile, Set<ElementEntry>> mapInputToElements = new HashMap<>();
    mapInputToElements.put(testInputFile, elementEntries);
    stateGraph.setMapInputToElements(mapInputToElements);
    // forward edge
    Map<ElementEntry, Set<GeneratedFile>> mapElementToGeneratedFiles = new HashMap<>();
    mapElementToGeneratedFiles.put(elementEntries.iterator().next(), generatedFiles);
    stateGraph.setMapElementToGeneratedFiles(mapElementToGeneratedFiles);

    // backward edge
    Map<GeneratedFile, Set<ElementEntry>> mapGeneratedFileToElements = new HashMap<>();
    mapGeneratedFileToElements.put(generatedFiles.iterator().next(), elementEntries);
    stateGraph.setMapGeneratedFileToElements(mapGeneratedFileToElements);
    // backward edge
    Map<ElementEntry, InputFile> mapElementToInputFiles = new HashMap<>();
    mapElementToInputFiles.put(elementEntries.iterator().next(), testInputFile);
    stateGraph.setMapElementToInputFiles(mapElementToInputFiles);

    return stateGraph;
  }
}
