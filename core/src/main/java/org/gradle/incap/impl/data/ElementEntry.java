package org.gradle.incap.impl.data;

import java.io.Serializable;
import javax.lang.model.element.Element;

public class ElementEntry implements Serializable {

    private Element element;
    private String eqlPath;

    public ElementEntry(Element element, String eqlPath) {
        this.eqlPath = eqlPath;
        this.element = element;
    }

    public ElementEntry(String eqlPath) {
        this.eqlPath = eqlPath;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementEntry that = (ElementEntry) o;

        return eqlPath.equals(that.eqlPath);
    }

    @Override
    public int hashCode() {
        return eqlPath.hashCode();
    }

    public String getEqlPath() {
        return eqlPath;
    }
}
