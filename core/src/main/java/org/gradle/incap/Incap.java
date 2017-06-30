package org.gradle.incap;

import org.gradle.incap.impl.ProccessorWorkFlowImpl;

/**
 * Entry point for instantiating API.
 */
public class Incap {

  // If Gradle and the AP are running in the same process, they should be
  // talking to the same workflow instance for simplicity.  So:  singleton for now.
  private static DefaultBuildWorkflow instance;
  private static ProccessorWorkFlowImpl defaultProcessorWorkflow;

  public static BuildWorkflow getBuildWorkflow() {
    if (instance == null) {
      instance = new DefaultBuildWorkflow();
    }
    return instance;
  }
  
  public static ProcessorWorkflow getProcessorWorkflow() {
    if (defaultProcessorWorkflow == null) {
      defaultProcessorWorkflow = new ProccessorWorkFlowImpl();
    }
    return defaultProcessorWorkflow;
  }
}
