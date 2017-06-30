package org.gradle.incap.impl.data;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class StateGraphTest {

  StateGraph stateGraphUnderTest;

  @org.junit.Test
  public void testAddGenerationEdge() throws Exception {
    //GIVEN
    stateGraphUnderTest = new StateGraph();
    GeneratedFile generatedFile = new GeneratedSourceFile("foo");
    ElementEntry entry1 = new ElementEntry("");

    //WHEN
    stateGraphUnderTest.addGenerationEdge(generatedFile, entry1);
    Set<ElementEntry> participatingElementEntries = stateGraphUnderTest.getParticipatingElementEntries(generatedFile);

    //THEN
    assertThat(participatingElementEntries.size(), is(1));
  }
}