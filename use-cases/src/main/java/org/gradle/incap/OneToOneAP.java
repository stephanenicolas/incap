package org.gradle.incap;

import static java.lang.String.format;
import static javax.lang.model.SourceVersion.latestSupported;
import static org.gradle.incap.Incap.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import org.gradle.incap.IncrementalFiler;
import org.gradle.incap.ProcessorWorkflow;
import org.gradle.incap.impl.data.GeneratedFile;
import org.gradle.incap.impl.data.GeneratedSourceFile;

public class OneToOneAP extends AbstractProcessor {

    private IncrementalFiler incrementalFiler;
    private ProcessorWorkflow processorWorkflow;
    private boolean isProcessingDone;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Filer filer = processingEnv.getFiler();
        processorWorkflow = getProcessorWorkflow();
        processorWorkflow.init(processingEnv);
        incrementalFiler = processorWorkflow.createIncrementalFiler(filer);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(OneToOneAP.class.getName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            System.out.println("Processing annotation:" + annotation);
        }

        if (isProcessingDone) {
            return false;
        }

        // generates a class with a constant that contains the name of all classes containing an annotation.
        Set<? extends Element> elementsAnnotatedWith =
            roundEnv.getElementsAnnotatedWith(Annotation1.class);
        
        Set<String> nameOfClassesContainingAnnotation1 = new HashSet<>();
        processElements(elementsAnnotatedWith, nameOfClassesContainingAnnotation1);

        for (String className : nameOfClassesContainingAnnotation1) {
            String generatedClassName = "APOneToOne_" + className + "Gen0";
            PrintWriter printWriter = null;
            try {
                JavaFileObject generatedObjectFile =
                    incrementalFiler.createSourceFile(
                        generatedClassName, toArray(elementsAnnotatedWith));
                Writer writer = generatedObjectFile.openWriter();
                printWriter = new PrintWriter(writer);
                String javaString = brewJava(generatedClassName);
                printWriter.append(javaString);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }
            }
        }

        isProcessingDone = true;
        return false;
    }

    private void processElements(
        Set<? extends Element> elementsAnnotatedWith,
        Set<String> nameOfClassesContainingAnnotation1) {
        for (Element elementWithAnnotation1 : elementsAnnotatedWith) {
            nameOfClassesContainingAnnotation1.add(getEnclosingClassName(elementWithAnnotation1));
        }
    }

    private Element[] toArray(Set<? extends Element> elementsAnnotatedWith) {
        Element[] result = new Element[elementsAnnotatedWith.size()];
        int index = 0;
        for (Element element : elementsAnnotatedWith) {
            System.out.println("element: " + element);
            result[index++] = element;
        }

        return result;
    }

    private String getEnclosingClassName(Element elementWithAnnotation1) {
        return elementWithAnnotation1.toString();
    }

    private String brewJava(String className) {
        StringBuilder builder = new StringBuilder();
        builder.append(format("public class %s {\n", className));
        builder.append("}");
        return builder.toString();
    }

    private String tab(int len) {
        StringBuilder builder = new StringBuilder();
        for (int space = 0; space < len; space++) {
            builder.append(' ');
        }
        return builder.toString();
    }
}
