package org.gradle.incap.impl.data;

import java.io.Serializable;

public abstract class GeneratedFile implements Serializable {
    public enum GeneratedFileType {
        SOURCE,
        CLASS,
        RESOURCE
    }

    abstract GeneratedFileType getType();
}
