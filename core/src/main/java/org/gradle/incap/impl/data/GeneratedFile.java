package org.gradle.incap.impl.data;

import java.util.List;
import javax.lang.model.element.Element;

public abstract class GeneratedFile {
    public enum GeneratedFileType {
        SOURCE,
        CLASS,
        RESOURCE
    }

    private GeneratedFileType type;
    private List<Element> originatingElements;
}
