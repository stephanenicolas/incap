package org.gradle.incap.impl.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.gradle.incap.impl.data.ElementEntry;
import org.gradle.incap.impl.data.GeneratedFile;
import org.gradle.incap.impl.data.GeneratedSourceFile;
import org.gradle.incap.impl.data.InputFile;
import org.gradle.incap.impl.data.StateGraph;

import static org.junit.Assert.assertEquals;

public class StateGraphUtilsTest {
    @org.junit.Test
    public void testSaveReadStateGraph() {
        StateGraph stateGraph = createStageGraph();
        StateGraphUtils.saveToFile(stateGraph);
        StateGraph generatedStateGraphFromFile = StateGraphUtils.readFromFile();

        Map<InputFile, Set<ElementEntry>> expectedInputToElementEdges = stateGraph.getMapInputToElements();
        Map<InputFile, Set<ElementEntry>> generatedInputToElementEdges = generatedStateGraphFromFile.getMapInputToElements();
        assertEquals(expectedInputToElementEdges.size(), generatedInputToElementEdges.size());
        Iterator<Map.Entry<InputFile, Set<ElementEntry>>> generatedInputToElementEdgesIterator = generatedInputToElementEdges.entrySet().iterator();
        for (Map.Entry<InputFile, Set<ElementEntry>> expectedEntrySet : expectedInputToElementEdges.entrySet()) {
            Map.Entry<InputFile, Set<ElementEntry>> generatedEntrySet = generatedInputToElementEdgesIterator.next();
            assertEquals(expectedEntrySet.getKey(), generatedEntrySet.getKey());
            assertEquals(expectedEntrySet.getValue(), generatedEntrySet.getValue());
        }

        Map<ElementEntry, Set<GeneratedFile>> expectedElementToGeneratedFileEdges = stateGraph.getMapElementToGeneratedFiles();
        Map<ElementEntry, Set<GeneratedFile>> generatedElementToGeneratedFileEdges = generatedStateGraphFromFile.getMapElementToGeneratedFiles();
        assertEquals(expectedElementToGeneratedFileEdges.size(), generatedElementToGeneratedFileEdges.size());
        Iterator<Map.Entry<ElementEntry, Set<GeneratedFile>>> generatedElementToGeneratedFileEdgeIterator = generatedElementToGeneratedFileEdges.entrySet().iterator();
        for (Map.Entry<ElementEntry, Set<GeneratedFile>> expectedEntrySet : expectedElementToGeneratedFileEdges.entrySet()) {
            Map.Entry<ElementEntry, Set<GeneratedFile>> generatedEntrySet = generatedElementToGeneratedFileEdgeIterator.next();
            assertEquals(expectedEntrySet.getKey(), generatedEntrySet.getKey());
            assertEquals(expectedEntrySet.getValue(), generatedEntrySet.getValue());
        }

        Map<GeneratedFile, Set<ElementEntry>> expectedGeneratedFileToElementEdges = stateGraph.getMapGeneratedFileToElements();
        Map<GeneratedFile, Set<ElementEntry>> generatedGeneratedFileToElementEdges = generatedStateGraphFromFile.getMapGeneratedFileToElements();
        assertEquals(expectedGeneratedFileToElementEdges.size(), generatedGeneratedFileToElementEdges.size());
        Iterator<Map.Entry<GeneratedFile, Set<ElementEntry>>> generatedGeneratedFileToElementEdgesIterator = generatedGeneratedFileToElementEdges.entrySet().iterator();
        for (Map.Entry<GeneratedFile, Set<ElementEntry>> expectedEntrySet : expectedGeneratedFileToElementEdges.entrySet()) {
            Map.Entry<GeneratedFile, Set<ElementEntry>> generatedEntrySet = generatedGeneratedFileToElementEdgesIterator.next();
            assertEquals(expectedEntrySet.getKey(), generatedEntrySet.getKey());
            assertEquals(expectedEntrySet.getValue(), generatedEntrySet.getValue());
        }

        Map<ElementEntry, InputFile> expectedElementToInputFilesEdges = stateGraph.getMapElementToInputFiles();
        Map<ElementEntry, InputFile> generatedElementToInputFilesEdges = generatedStateGraphFromFile.getMapElementToInputFiles();
        assertEquals(expectedElementToInputFilesEdges.size(), generatedElementToInputFilesEdges.size());
        Iterator<Map.Entry<ElementEntry, InputFile>> generatedElementToInputFilesEdgesIterator = generatedElementToInputFilesEdges.entrySet().iterator();
        for (Map.Entry<ElementEntry, InputFile> expectedEntrySet : expectedElementToInputFilesEdges.entrySet()) {
            Map.Entry<ElementEntry, InputFile> generatedEntrySet = generatedElementToInputFilesEdgesIterator.next();
            assertEquals(expectedEntrySet.getKey(), generatedEntrySet.getKey());
            assertEquals(expectedEntrySet.getValue(), generatedEntrySet.getValue());
        }
    }

    private StateGraph createStageGraph() {
        StateGraph stateGraph = new StateGraph(null, null);

        InputFile testInputFile = new InputFile("inputFile.java");

        Set<ElementEntry> elementEntries = new HashSet<>();
        elementEntries.add(new ElementEntry(null, "element entry"));

        Set<GeneratedFile> generatedFiles = new HashSet<>();
        generatedFiles.add(new GeneratedSourceFile("generatedFile.java"));

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
