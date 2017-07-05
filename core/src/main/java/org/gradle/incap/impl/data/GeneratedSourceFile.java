package org.gradle.incap.impl.data;

public class GeneratedSourceFile extends GeneratedFile {
    private CharSequence name;

    public GeneratedSourceFile(CharSequence name) {
        this.name = name;
    }

    public CharSequence getName() {
        return name;
    }

    @Override
    public GeneratedFileType getType() {
        return GeneratedFileType.SOURCE;
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

        GeneratedSourceFile that = (GeneratedSourceFile) o;

        return name.equals(that.name);
    }
}
