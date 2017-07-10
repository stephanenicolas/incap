package org.gradle.incap.impl.utils;

import java.util.Map;
import java.util.Set;
import org.gradle.incap.impl.data.ElementEntry;
import org.gradle.incap.impl.data.GeneratedFile;
import org.gradle.incap.impl.data.InputFile;
import org.gradle.incap.impl.data.StateGraph;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.in;

public class StateGraphMatcher {
  /**
   * @return the reason why they differ
   */
  public static void assertEquals(StateGraph expected, StateGraph actual) {
    Map<InputFile, Set<ElementEntry>> actualInputToElementEdges = actual.getMapInputToElements();
    Map<InputFile, Set<ElementEntry>> expectedInputToElementEdges = expected.getMapInputToElements();
    compareSGMap("mapInputToElements", actualInputToElementEdges, expectedInputToElementEdges);

    Map<ElementEntry, Set<GeneratedFile>> actualElementToGeneratedFileEdges = actual.getMapElementToGeneratedFiles();
    Map<ElementEntry, Set<GeneratedFile>> expectedElementToGeneratedFileEdges = expected.getMapElementToGeneratedFiles();
    compareSGMap("mapElementToGeneratedFiles", actualElementToGeneratedFileEdges, expectedElementToGeneratedFileEdges);

    Map<GeneratedFile, Set<ElementEntry>> actualGeneratedFileToElementEdges = actual.getMapGeneratedFileToElements();
    Map<GeneratedFile, Set<ElementEntry>> expectedGeneratedFileToElementEdges = expected.getMapGeneratedFileToElements();
    compareSGMap("mapGeneratedFileToElements", actualGeneratedFileToElementEdges, expectedGeneratedFileToElementEdges);

    Map<ElementEntry, InputFile> actualElementToInputFilesEdges = actual.getMapElementToInputFiles();
    Map<ElementEntry, InputFile> expectedElementToInputFilesEdges = expected.getMapElementToInputFiles();
    compareSGMap("mapElementToInputFiles", actualElementToInputFilesEdges, expectedElementToInputFilesEdges);
  }

  private static <T, U> void compareSGMap(String mapName, Map<T, U> actualInputToElementEdges, Map<T, U> expectedInputToElementEdges) {
    assertThat("SGs differ in " + mapName + " sizes", expectedInputToElementEdges.size(), is(actualInputToElementEdges.size()));
    assertThat("Expected SG has more elements in " + mapName, expectedInputToElementEdges.entrySet(),
        everyItem(is(in(actualInputToElementEdges.entrySet()))));
    assertThat("Actual SG has more elements in " + mapName, actualInputToElementEdges.entrySet(),
        everyItem(is(in(expectedInputToElementEdges.entrySet()))));
  }
}
