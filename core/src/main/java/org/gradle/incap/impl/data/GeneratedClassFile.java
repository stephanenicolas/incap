package org.gradle.incap.impl.data;

public class GeneratedClassFile extends GeneratedFile {
    private CharSequence name;

    public GeneratedClassFile(CharSequence name) {
        this.name = name;
    }

    public CharSequence getName() {
        return name;
    }

    @Override
    public GeneratedFileType getType() {
        return GeneratedFileType.CLASS;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneratedClassFile that = (GeneratedClassFile) o;

        return name.equals(that.name);
    }
}
