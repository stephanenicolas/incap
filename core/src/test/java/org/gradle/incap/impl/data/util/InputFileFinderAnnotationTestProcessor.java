package org.gradle.incap.impl.data.util;

import static javax.lang.model.SourceVersion.latestSupported;

import org.gradle.incap.Annotation1;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.gradle.incap.impl.data.InputFile;
import org.gradle.incap.impl.data.InputFileFinder;

@SupportedAnnotationTypes("*")
public class InputFileFinderAnnotationTestProcessor extends AbstractProcessor {

    private InputFileFinder inputFileFinder = new InputFileFinder();
    private InputFile inputFileForElement;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (roundEnv.processingOver()) {
            return false;
        }

        Set<? extends Element> elementsAnnotatedWith =
                roundEnv.getElementsAnnotatedWith(Annotation1.class);

        if (elementsAnnotatedWith.size() != 1) {
            throw new IllegalStateException(
                    "There must be exactly one element to process. Actual="
                            + elementsAnnotatedWith.size());
        }

        for (Element element : elementsAnnotatedWith) {
            inputFileForElement = inputFileFinder.findInputFileForElement(element);
        }

        return false;
    }

    public InputFile getInputFileForElement() {
        return inputFileForElement;
    }
}
