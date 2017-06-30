package com.example;

import static javax.lang.model.SourceVersion.latestSupported;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.gradle.incap.IncrementalFiler;

public class Processor1 extends AbstractProcessor {

    private IncrementalFiler incrementalFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Filer filer = processingEnv.getFiler();
        incrementalFiler = new IncrementalFiler(filer);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add("com.example.Annotation1");
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

        try {
            incrementalFiler.createSourceFile("foo", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
