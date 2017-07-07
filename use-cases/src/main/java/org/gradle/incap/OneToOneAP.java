package org.gradle.incap;

import static java.lang.String.format;
import static javax.lang.model.SourceVersion.latestSupported;
import static org.gradle.incap.Incap.*;
import static org.gradle.incap.util.APUtil.*;
import static org.gradle.incap.util.APUtil.generateFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class OneToOneAP extends AbstractProcessor {

    private IncrementalFiler incrementalFiler;
    private ProcessorWorkflow processorWorkflow;
    private boolean isProcessingDone;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Filer filer = processingEnv.getFiler();
        processorWorkflow = getProcessorWorkflow();
        processorWorkflow.init(processingEnv);
        incrementalFiler = processorWorkflow.createIncrementalFiler(filer);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Annotation1.class.getName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            System.out.println("Processing annotation:" + annotation);
        }

        if (isProcessingDone) {
            return false;
        }

        // generates a class with a constant that contains the name of all classes containing an annotation.
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Annotation1.class);
        Map<String, Set<Element>> mapGeneratedFileNameToOriginatingElements = processElements(annotatedElements);
        generateFiles(incrementalFiler, mapGeneratedFileNameToOriginatingElements);
        isProcessingDone = true;
        return false;
    }

    private Map<String, Set<Element>> processElements(Set<? extends Element> annotatedElements) {
        final Map<String, Set<Element>> mapGeneratedFileNameToOrginatingElements = new HashMap<>();
        for (Element annotatedElement : annotatedElements) {
            String nameOfClassContainingElement = getEnclosingClassName(annotatedElement);
            final String finalClassName = getClass().getSimpleName() + "_" + nameOfClassContainingElement;
            mapGeneratedFileNameToOrginatingElements.put(finalClassName, Collections.singleton(annotatedElement));
        }
        return mapGeneratedFileNameToOrginatingElements;
    }

}
