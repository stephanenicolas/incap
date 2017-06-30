package org.gradle.incap;

/**
 * Entry point for instantiating API.
 */
public class Incap {

  // If Gradle and the AP are running in the same process, they should be
  // talking to the same workflow instance for simplicity.  So:  singleton for now.
  private static DefaultBuildWorkflow instance;

  public static BuildWorkflow getBuildWorkflow() {
    if (instance == null) {
      instance = new DefaultBuildWorkflow();
    }
    return instance;
  }
}
