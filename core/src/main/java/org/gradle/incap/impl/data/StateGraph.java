package org.gradle.incap.impl.data;

import com.gradle.incap.AnnotationPathEncoder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.Element;

public class StateGraph implements Serializable {
    //forward edges
    private Map<InputFile, Set<ElementEntry>> mapInputToElements = new HashMap<>();
    private Map<ElementEntry, Set<GeneratedFile>> mapElementToGeneratedFiles = new HashMap<>();

    //backward edges
    private Map<GeneratedFile, Set<ElementEntry>> mapGeneratedFileToElements = new HashMap<>();
    private Map<ElementEntry, InputFile> mapElementToInputFiles = new HashMap<>();

    private final AnnotationPathEncoder annotationPathEncoder;
    private final InputFileFinder inputFileFinder;

    public StateGraph(
            AnnotationPathEncoder annotationPathEncoder,
            InputFileFinder inputFileFinder) {
        this.annotationPathEncoder = annotationPathEncoder;
        this.inputFileFinder = inputFileFinder;
    }

    /** The incremental filer will call this one. */
    public void addSourceFile(CharSequence name, Element... originatingElements) {
        GeneratedFile generatedFile = new GeneratedSourceFile(name);

        ElementEntry[] originatingElementEntries = new ElementEntry[originatingElements.length];
        for (int i = 0; i < originatingElementEntries.length; i++) {
            Element originatingElement = originatingElements[i];
            System.out.println(" originatingElement annotated with: " + originatingElement);
            originatingElementEntries[i] =
                    new ElementEntry(
                            originatingElement,
                            annotationPathEncoder.encodeClass(originatingElement));
        }

        addGenerationEdge(generatedFile, originatingElementEntries);
    }

    /* package-private*/ void addGenerationEdge(
            GeneratedFile generatedFile, ElementEntry... originatingElements) {
        addBackwardEdgeFromGeneratedFileToElements(generatedFile, originatingElements);
        addBackwardEdgeFromElementToInputFiles(originatingElements);
        addForwardEdgeFromElementToGeneratedFiles(generatedFile, originatingElements);
        addForwardEdgeFromInputToElements(originatingElements);
    }

    private void addForwardEdgeFromInputToElements(ElementEntry... originatingElements) {
        for (ElementEntry elementEntry : originatingElements) {
            InputFile inputFile =
                    inputFileFinder.findInputFileForElement(elementEntry.getElement());

            Set<ElementEntry> existingElementEntries = mapInputToElements.get(inputFile);
            if (existingElementEntries == null) {
                existingElementEntries = new HashSet<>();
            }

            existingElementEntries.add(elementEntry);

            mapInputToElements.put(inputFile, existingElementEntries);
        }
    }

    private void addBackwardEdgeFromElementToInputFiles(ElementEntry... originatingElements) {
        for (ElementEntry elementEntry : originatingElements) {
            mapElementToInputFiles.put(
                    elementEntry,
                    inputFileFinder.findInputFileForElement(elementEntry.getElement()));
        }
    }

    private void addForwardEdgeFromElementToGeneratedFiles(
            GeneratedFile generatedFile, ElementEntry... originatingElements) {
        for (ElementEntry originatingElement : originatingElements) {
            Set<GeneratedFile> elementEntries = mapElementToGeneratedFiles.get(originatingElement);
            if (elementEntries == null) {
                elementEntries = new HashSet<>();
            }
            elementEntries.add(generatedFile);
            mapElementToGeneratedFiles.put(originatingElement, elementEntries);
        }
    }

    private void addBackwardEdgeFromGeneratedFileToElements(
            GeneratedFile generatedFile, ElementEntry... originatingElements) {
        Set<ElementEntry> originatingElementEntries = new HashSet<>();
        for (ElementEntry originatingElement : originatingElements) {
            originatingElementEntries.add(originatingElement);
        }

        mapGeneratedFileToElements.put(generatedFile, originatingElementEntries);
    }

    public Set<Element> getParticipatingElements(GeneratedFile generatedFile) {
        Set<ElementEntry> participatingElementEntries =
                getParticipatingElementEntries(generatedFile);
        Set<Element> elements = new HashSet<>();
        for (ElementEntry participatingElementEntry : participatingElementEntries) {
            elements.add(participatingElementEntry.getElement());
        }
        return elements;
    }

    public Set<ElementEntry> getParticipatingElementEntries(GeneratedFile generatedFile) {
        return mapGeneratedFileToElements.get(generatedFile);
    }

    public Map<InputFile, Set<ElementEntry>> getMapInputToElements() {
        return mapInputToElements;
    }

    public void setMapInputToElements(Map<InputFile, Set<ElementEntry>> mapInputToElements) {
        this.mapInputToElements = mapInputToElements;
    }

    public Map<ElementEntry, Set<GeneratedFile>> getMapElementToGeneratedFiles() {
        return mapElementToGeneratedFiles;
    }

    public void setMapElementToGeneratedFiles(Map<ElementEntry, Set<GeneratedFile>> mapElementToGeneratedFiles) {
        this.mapElementToGeneratedFiles = mapElementToGeneratedFiles;
    }

    public Map<GeneratedFile, Set<ElementEntry>> getMapGeneratedFileToElements() {
        return mapGeneratedFileToElements;
    }

    public void setMapGeneratedFileToElements(Map<GeneratedFile, Set<ElementEntry>> mapGeneratedFileToElements) {
        this.mapGeneratedFileToElements = mapGeneratedFileToElements;
    }

    public Map<ElementEntry, InputFile> getMapElementToInputFiles() {
        return mapElementToInputFiles;
    }

    public void setMapElementToInputFiles(Map<ElementEntry, InputFile> mapElementToInputFiles) {
        this.mapElementToInputFiles = mapElementToInputFiles;
    }
}
