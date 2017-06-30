package com.gradle.incap;

import static com.gradle.incap.QueryLanguage.LOCATOR_ANNOTATION_ON_FIELD;
import static com.gradle.incap.QueryLanguage.LOCATOR_CLASS;
import static com.gradle.incap.QueryLanguage.SEPARATOR;

import java.lang.annotation.Annotation;
import javax.lang.model.element.Element;

public class AnnotationPathEncoder {

    public String encodeAnnotationOnField(
            Element fieldElement, Class<? extends Annotation> annotationClass) {
        final String className = fieldElement.getEnclosingElement().toString();
        final String fieldName = fieldElement.getSimpleName().toString();
        final String annotationTypeName = annotationClass.getName();
        return new StringBuilder()
                .append(LOCATOR_ANNOTATION_ON_FIELD)
                .append(SEPARATOR)
                .append(className)
                .append(SEPARATOR)
                .append(fieldName)
                .append(SEPARATOR)
                .append(annotationTypeName)
                .toString();
    }

  public String encodeClass(Element classElement) {
    System.out.println(" classElement annotated with: " + classElement);

    final String className = classElement.toString();
    return new StringBuilder()
        .append(LOCATOR_CLASS)
        .append(SEPARATOR)
        .append(className)
        .toString();
  }

}
