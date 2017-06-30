package org.gradle.incap.impl.data;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

public class StateGraphTest {

  StateGraph stateGraphUnderTest;

  @org.junit.Test
  public void testAddGenerationEdge_shouldReturnSingleParticipatingElement() throws Exception {
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

  @org.junit.Test
  public void testAddGenerationEdge_shouldReturnSingleParticipatingElements() throws Exception {
    //GIVEN
    stateGraphUnderTest = new StateGraph();
    GeneratedFile generatedFile = new GeneratedSourceFile("foo");
    ElementEntry entry1 = new ElementEntry("abc");
    ElementEntry entry2 = new ElementEntry("bcd");

    //WHEN
    stateGraphUnderTest.addGenerationEdge(generatedFile, entry1, entry2);
    Set<ElementEntry> participatingElementEntries = stateGraphUnderTest.getParticipatingElementEntries(generatedFile);

    //THEN
    assertThat(participatingElementEntries.size(), is(2));
    assertThat(participatingElementEntries, hasItems(entry1, entry2));
  }
}