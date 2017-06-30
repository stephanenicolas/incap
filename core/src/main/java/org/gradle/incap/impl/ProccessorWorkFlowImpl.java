package org.gradle.incap.impl;

import com.gradle.incap.AnnotationFinder;
import com.gradle.incap.AnnotationPathEncoder;
import java.io.File;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import org.gradle.incap.IncrementalFiler;
import org.gradle.incap.ProcessorWorkflow;
import org.gradle.incap.impl.data.StateGraph;

public class ProccessorWorkFlowImpl implements ProcessorWorkflow {

  private boolean isIncremental;
  private StateGraph stateGraph = new StateGraph();
  private AnnotationFinder annotationFinder = new AnnotationFinder();
  private AnnotationPathEncoder annotationPathEncoder = new AnnotationPathEncoder();
  private Filer filer;
  private Elements elementUtils;
  private Messager messager;

  @Override
  public boolean isIncremental() {
    return isIncremental;
  }

  @Override
  public IncrementalFiler init(ProcessingEnvironment processingEnv) {
    filer = processingEnv.getFiler();
    elementUtils = processingEnv.getElementUtils();
    messager = processingEnv.getMessager();
    annotationFinder = new AnnotationFinder(elementUtils);
    annotationPathEncoder = new AnnotationPathEncoder();
    return new IncrementalFiler(filer);
  }

  @Override
  public void process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

  }

  @Override
  public Set<Element> getParticipatingElements(File target) {
    return null;
  }

  @Override
  public StateGraph getStateGraph() {
    return stateGraph;
  }
}
