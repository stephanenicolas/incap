package org.gradle.incap.impl;

import com.gradle.incap.AnnotationFinder;
import com.gradle.incap.AnnotationPathEncoder;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import org.gradle.incap.IncrementalFiler;
import org.gradle.incap.ProcessorWorkflow;
import org.gradle.incap.impl.data.ElementEntry;
import org.gradle.incap.impl.data.GeneratedFile;
import org.gradle.incap.impl.data.InputFileFinder;
import org.gradle.incap.impl.data.StateGraph;

public class ProccessorWorkFlowImpl implements ProcessorWorkflow {

    private boolean isIncremental;
    private StateGraph stateGraph;
    private AnnotationFinder annotationFinder;
    private AnnotationPathEncoder annotationPathEncoder;
    private Filer filer;
    private Elements elementUtils;

    @Override
    public boolean isIncremental() {
        return isIncremental;
    }

    @Override
    public IncrementalFiler init(ProcessingEnvironment processingEnv) {
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
        annotationFinder = new AnnotationFinder(elementUtils);
        annotationPathEncoder = new AnnotationPathEncoder();
        stateGraph = new StateGraph(annotationPathEncoder, new InputFileFinder());
        return new IncrementalFiler(filer);
    }

    @Override
    public void process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {}

    @Override
    public Set<Element> getParticipatingElements(GeneratedFile target) {
        Set<ElementEntry> participatingElementEntries =
                stateGraph.getParticipatingElementEntries(target);
        Set<Element> elements = new HashSet<>();
        if (participatingElementEntries != null) {
            for (ElementEntry participatingElementEntry : participatingElementEntries) {
                Element element = participatingElementEntry.getElement();
                if (element == null) {
                    element =
                            annotationFinder.lookupElement(participatingElementEntry.getEqlPath());
                    participatingElementEntry.setElement(element);
                }
                elements.add(element);
            }
        }
        return elements;
    }

    @Override
    public StateGraph getStateGraph() {
        return stateGraph;
    }

    @Override
    public IncrementalFiler createIncrementalFiler(Filer filer) {
        return new IncrementalFiler(filer);
    }
}
