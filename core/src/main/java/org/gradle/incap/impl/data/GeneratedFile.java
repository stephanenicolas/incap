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

    private GeneratedFileType type;
    private List<Element> originatingElements = new ArrayList<>();

    public GeneratedFileType getType() {
        return type;
    }

    public List<Element> getOriginatingElements() {
        return originatingElements;
    }
}
