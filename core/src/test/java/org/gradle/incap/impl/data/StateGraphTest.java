package org.gradle.incap.impl.data;

import com.gradle.incap.AnnotationFinder;
import com.gradle.incap.AnnotationPathEncoder;
import java.util.Set;
import javax.lang.model.element.Element;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

public class StateGraphTest {

    StateGraph stateGraphUnderTest;

    @org.junit.Test
    public void testAddGenerationEdge_shouldReturnSingleParticipatingElement() throws Exception {
        //GIVEN
        AnnotationFinder mockAnnotationFinder = createMock(AnnotationFinder.class);
        AnnotationPathEncoder mockAnnotationPathEncoder = createMock(AnnotationPathEncoder.class);
        InputFileFinder mockInputFileFinder = createMock(InputFileFinder.class);
        InputFile inputFile = new InputFile("Foo.class");
        expect(mockInputFileFinder.findInputFileForElement((Element) anyObject()))
                .andReturn(inputFile)
                .times(2);
        stateGraphUnderTest =
                new StateGraph(
                        mockAnnotationPathEncoder, mockInputFileFinder);
        GeneratedFile generatedFile = new GeneratedSourceFile("GeneratedFoo");
        ElementEntry entry1 = new ElementEntry("");
        replay(mockAnnotationFinder, mockAnnotationPathEncoder, mockInputFileFinder);

        //WHEN
        stateGraphUnderTest.addGenerationEdge(generatedFile, entry1);
        Set<ElementEntry> participatingElementEntries =
                stateGraphUnderTest.getParticipatingElementEntries(generatedFile);

        //THEN
        assertThat(participatingElementEntries.size(), is(1));
        verify(mockAnnotationFinder, mockAnnotationPathEncoder, mockInputFileFinder);
    }

    @org.junit.Test
    public void testAddGenerationEdge_shouldReturnSingleParticipatingElements() throws Exception {
        //GIVEN
        AnnotationFinder mockAnnotationFinder = createMock(AnnotationFinder.class);
        AnnotationPathEncoder mockAnnotationPathEncoder = createMock(AnnotationPathEncoder.class);
        InputFileFinder mockInputFileFinder = createMock(InputFileFinder.class);
        InputFile inputFile = new InputFile("Foo.class");
        expect(mockInputFileFinder.findInputFileForElement((Element) anyObject()))
                .andReturn(inputFile)
                .times(4);
        stateGraphUnderTest =
                new StateGraph(
                        mockAnnotationPathEncoder, mockInputFileFinder);
        GeneratedFile generatedFile = new GeneratedSourceFile("GeneratedFoo");
        ElementEntry entry1 = new ElementEntry("abc");
        ElementEntry entry2 = new ElementEntry("bcd");
        replay(mockAnnotationFinder, mockAnnotationPathEncoder, mockInputFileFinder);

        //WHEN
        stateGraphUnderTest.addGenerationEdge(generatedFile, entry1, entry2);
        Set<ElementEntry> participatingElementEntries =
                stateGraphUnderTest.getParticipatingElementEntries(generatedFile);

        //THEN
        assertThat(participatingElementEntries.size(), is(2));
        assertThat(participatingElementEntries, hasItems(entry1, entry2));
        verify(mockAnnotationFinder, mockAnnotationPathEncoder, mockInputFileFinder);
    }
}
