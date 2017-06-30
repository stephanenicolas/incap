package org.gradle.incap.impl.data;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;

public abstract class GeneratedFile {
    public enum GeneratedFileType {
        SOURCE,
        CLASS,
        RESOURCE
    }

    protected List<Element> originatingElements = new ArrayList<>();

    abstract GeneratedFileType getType();

    public List<Element> getOriginatingElements() {
        return originatingElements;
    }
}
