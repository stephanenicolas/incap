package org.gradle.incap.impl.data;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * The following code is copied from NetBeans
 * http://grepcode.com/file/bits.netbeans.org/maven2/org.netbeans.api/org-netbeans-modules-java-source/RELEASE68/org/netbeans/api/java/source/ElementUtilities.java#129
 */
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
