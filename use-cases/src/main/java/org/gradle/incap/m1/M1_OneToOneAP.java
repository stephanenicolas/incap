package org.gradle.incap.m1;

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
import org.gradle.incap.Annotation1;
import org.gradle.incap.IncrementalFiler;
import org.gradle.incap.ProcessorWorkflow;

import static javax.lang.model.SourceVersion.latestSupported;
import static org.gradle.incap.Incap.getProcessorWorkflow;
import static org.gradle.incap.util.APUtil.generateFiles;
import static org.gradle.incap.util.APUtil.getEnclosingClassName;

/**
 * This AP creates 1 target file for 1 source file.
 * This case should be easy to handle as we actually don't need incap at all,
 * the processor is already incremental and the filer will provide the build
 * system information about generated files that should be deleted when their
 * corresponding input file is deleted.
 */
public class M1_OneToOneAP extends AbstractProcessor {

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
    Map<String, Set<? extends Element>> mapGeneratedFileNameToOriginatingElements = processElements(annotatedElements);
    generateFiles(incrementalFiler, mapGeneratedFileNameToOriginatingElements);
    isProcessingDone = true;
    return false;
  }

  private Map<String, Set<? extends Element>> processElements(Set<? extends Element> annotatedElements) {
    final Map<String, Set<? extends Element>> mapGeneratedFileNameToOrginatingElements = new HashMap<>();
    for (Element annotatedElement : annotatedElements) {
      String nameOfClassContainingElement = getEnclosingClassName(annotatedElement);
      final String finalClassName = getClass().getSimpleName() + "_" + nameOfClassContainingElement + "Gen0";
      mapGeneratedFileNameToOrginatingElements.put(finalClassName, Collections.singleton(annotatedElement));
    }
    return mapGeneratedFileNameToOrginatingElements;
  }
}
