package org.gradle.incap.impl;

import java.io.File;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.gradle.incap.IncrementalFiler;
import org.gradle.incap.ProcessorWorkflow;

public class ProccessorWorkFlowImpl implements ProcessorWorkflow {
  @Override
  public boolean isIncremental() {
    return false;
  }

  @Override
  public IncrementalFiler init(ProcessingEnvironment processingEnv) {
    return null;
  }

  @Override
  public void process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

  }

  @Override
  public Set<Element> getParticipatingElements(File target) {
    return null;
  }
}
