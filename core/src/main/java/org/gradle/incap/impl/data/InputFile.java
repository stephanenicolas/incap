package org.gradle.incap.impl.data;

import java.io.Serializable;

public class InputFile implements Serializable {

    private String name;

    public InputFile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InputFile that = (InputFile) o;

        return name.equals(that.name);
    }
}
