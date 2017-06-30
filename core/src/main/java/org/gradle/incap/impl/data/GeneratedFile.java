package org.gradle.incap.impl.data;

public abstract class GeneratedFile {
    public enum GeneratedFileType {
        SOURCE,
        CLASS,
        RESOURCE
    }

    abstract GeneratedFileType getType();
}
