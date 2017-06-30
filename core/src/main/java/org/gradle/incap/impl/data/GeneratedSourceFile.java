package org.gradle.incap.impl.data;

import java.util.List;
import javax.lang.model.element.Element;

public class GeneratedSourceFile extends GeneratedFile {
    private CharSequence name;

    public GeneratedSourceFile(CharSequence name, List<Element> originatingElements) {
        this.name = name;
        this.originatingElements = originatingElements;
    }

    public CharSequence getName() {
        return name;
    }

    @Override
    public GeneratedFileType getType() {
        return GeneratedFileType.SOURCE;
    }
}
