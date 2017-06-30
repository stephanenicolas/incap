package org.gradle.incap.impl.data;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

public class ElementUtils {
    public static TypeElement enclosingTypeElement(Element element) throws IllegalArgumentException {
        return enclosingTypeElementImpl(element);
    }

    private static TypeElement enclosingTypeElementImpl( Element element ) throws IllegalArgumentException {

        if( element.getKind() == ElementKind.PACKAGE ) {
            throw new IllegalArgumentException();
        }

        element = element.getEnclosingElement();

        if (element.getKind() == ElementKind.PACKAGE) {
            //element is a top level class, returning null according to the contract:
            return null;
        }

        while(element != null && !(element.getKind().isClass() || element.getKind().isInterface())) {
            element = element.getEnclosingElement();
        }

        return (TypeElement)element;
    }
}
