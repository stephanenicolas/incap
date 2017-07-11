package org.gradle.incap.impl.data;

import java.io.Serializable;

public abstract class GeneratedFile implements Serializable {
    public static final String FIELD_SEPARATOR = ":";

    public enum GeneratedFileType {
        SOURCE,
        CLASS,
        RESOURCE
    }

    abstract GeneratedFileType getType();

    @Override
    public String toString() {
        return getType().toString();
    }
}
