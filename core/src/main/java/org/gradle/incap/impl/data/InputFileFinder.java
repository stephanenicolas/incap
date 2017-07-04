package org.gradle.incap.impl.data;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

public class InputFileFinder {

    public InputFile findInputFileForElement(Element element) {
        TypeElement enclosingTypeElement = enclosingTypeElement(element);
        String className = enclosingTypeElement.getQualifiedName().toString();
        return new InputFile(className);
    }

    private TypeElement enclosingTypeElement(Element element) {

        if (element.getKind() == ElementKind.PACKAGE) {
            throw new IllegalArgumentException();
        }

        while (element != null && !isType(element)) {
            element = element.getEnclosingElement();
        }

        return (TypeElement) element;
    }

    private static boolean isType(Element element) {
        return element.getKind().isClass() || element.getKind().isInterface();
    }
}
