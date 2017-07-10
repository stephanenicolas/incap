package org.gradle.incap.impl.utils;

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
import org.gradle.incap.impl.data.InputFile;
import org.gradle.incap.impl.data.StateGraph;

public class StateGraphUtils {
  public static final String M1_FORMAT_VERSION = "1";

  public void exportToM1Format(PrintWriter writer, StateGraph stateGraph) {
    Map<GeneratedFile, Set<InputFile>> mapGeneratedFileToInputFile = new HashMap<>(stateGraph.getMapGeneratedFileToElements().keySet().size());
    for (Map.Entry<GeneratedFile, Set<ElementEntry>> entryGeneratedFileToElements : stateGraph.getMapGeneratedFileToElements().entrySet()) {
      GeneratedFile generatedFile = entryGeneratedFileToElements.getKey();
      Set<InputFile> inputFiles = new HashSet<>();
      Set<ElementEntry> elements = entryGeneratedFileToElements.getValue();
      for (ElementEntry element : elements) {
        inputFiles.add(stateGraph.getMapElementToInputFiles().get(element));
      }
      mapGeneratedFileToInputFile.put(generatedFile, inputFiles);
    }

    writer.println(M1_FORMAT_VERSION);
    for (Map.Entry<GeneratedFile, Set<InputFile>> entryGeneratedFileToInputFile : mapGeneratedFileToInputFile.entrySet()) {
      GeneratedFile generatedFile = entryGeneratedFileToInputFile.getKey();
      Set<InputFile> inputFiles = entryGeneratedFileToInputFile.getValue();
      writer.println(generatedFile.toString() + " --> " + inputFiles.toString());
    }
    writer.flush();
  }

  public void saveToStream(ObjectOutputStream outputStream, StateGraph stateGraph) throws IOException {
    if (stateGraph == null) {
      throw new IllegalStateException("state graph can't be null.");
    }

    outputStream.writeObject(stateGraph);
    outputStream.flush();
  }

  public StateGraph readFromStream(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
    return (StateGraph) inputStream.readObject();
  }
}
