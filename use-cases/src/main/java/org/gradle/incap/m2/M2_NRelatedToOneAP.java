package org.gradle.incap.m2;

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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import org.gradle.incap.Annotation1;
import org.gradle.incap.IncrementalFiler;
import org.gradle.incap.ProcessorWorkflow;
import org.gradle.incap.impl.data.GeneratedFile;
import org.gradle.incap.impl.data.GeneratedSourceFile;

import static java.util.Arrays.asList;
import static javax.lang.model.SourceVersion.latestSupported;
import static org.gradle.incap.Incap.getProcessorWorkflow;
import static org.gradle.incap.util.APUtil.generateFiles;
import static org.gradle.incap.util.APUtil.getEnclosingClassName;

/**
 * This AP creates 1 target file for N related input files.
 * Related means that we can retrieve the other files from one of them.
 * This case should be easy to handle as we actually don't need incap at all,
 * the processor is already incremental and the filer will provide the build
 * system information about generated files that should be deleted when their
 * corresponding input file is deleted.
 * With Milestone 1 (when incap re-passes to javac all source files),
 * AP is not impacted.
 * With Milestone 2 (when incap will retrieve the elements of a FB), some APs
 * can get faster as they might rely on incap to retrieve annotated elements
 * that were already processed and might not need to walk the tree anymore.
 */
public class M2_NRelatedToOneAP extends AbstractProcessor {

  private IncrementalFiler incrementalFiler;
  private ProcessorWorkflow processorWorkflow;
  private boolean isProcessingDone;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    Filer filer = processingEnv.getFiler();
    if (processorWorkflow == null) {
      processorWorkflow = getProcessorWorkflow();
    }
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
    Map<String, Set<? extends Element>> mapGeneratedFileNameToOriginatingElements = processElements(annotatedElements);
    checkForParticipating(mapGeneratedFileNameToOriginatingElements);
    generateFiles(incrementalFiler, mapGeneratedFileNameToOriginatingElements);
    isProcessingDone = true;
    return false;
  }

  private void checkForParticipating(Map<String, Set<? extends Element>> mapGeneratedFileNameToOriginatingElements) {
    for (Map.Entry<String, Set<? extends Element>> entry : mapGeneratedFileNameToOriginatingElements.entrySet()) {
      GeneratedFile generatedFile = new GeneratedSourceFile(entry.getKey());
      Set<Element> participatingElements = processorWorkflow.getParticipatingElements(generatedFile);
      if (!participatingElements.isEmpty()) {
        Set<Element> newElements = new HashSet<>(participatingElements);
        newElements.addAll(entry.getValue());
        mapGeneratedFileNameToOriginatingElements.putAll(processElements(newElements));
      }
    }
  }

  private Map<String, Set<? extends Element>> processElements(Set<? extends Element> annotatedElements) {
    final Map<String, Set<? extends Element>> mapGeneratedFileNameToOrginatingElements = new HashMap<>();
    for (Element annotatedElement : annotatedElements) {
      //process class
      String nameOfClassContainingElement = getEnclosingClassName(annotatedElement);
      final String finalClassName0 = getClass().getSimpleName() + "_" + nameOfClassContainingElement + "Gen0";
      mapGeneratedFileNameToOrginatingElements.put(finalClassName0, Collections.singleton(annotatedElement));

      //process super clas
      TypeMirror superclass = ((TypeElement) annotatedElement).getSuperclass();
      Element superClassAsElement = ((DeclaredType) superclass).asElement();
      if (superClassAsElement.getAnnotation(Annotation1.class) != null) {
        String nameOfSuperClassContainingElement = getEnclosingClassName(superClassAsElement);
        final String finalClassName1 = getClass().getSimpleName() //
            + "_" //
            + nameOfClassContainingElement //
            + "_" //
            + nameOfSuperClassContainingElement //
            + "Gen0";
        mapGeneratedFileNameToOrginatingElements.put(finalClassName1, new HashSet<>(asList(annotatedElement, superClassAsElement)));
      }
    }
    return mapGeneratedFileNameToOrginatingElements;
  }
}
