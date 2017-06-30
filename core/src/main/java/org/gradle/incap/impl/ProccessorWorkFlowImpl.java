package org.gradle.incap.impl;

import java.io.File;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.gradle.incap.IncrementalFiler;
import org.gradle.incap.ProcessorWorkflow;
import org.gradle.incap.impl.data.StateGraph;

public class ProccessorWorkFlowImpl implements ProcessorWorkflow {

  private boolean isIncremental;
  private StateGraph stateGraph = new StateGraph();

  @Override
  public boolean isIncremental() {
    return isIncremental;
  }

  @Override
  public IncrementalFiler init(ProcessingEnvironment processingEnv) {
    Filer filer = processingEnv.getFiler();
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
