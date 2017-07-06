package org.gradle.incap.impl.data;

import javax.tools.JavaFileManager;

public class GeneratedResourceFile extends GeneratedFile {
    private JavaFileManager.Location location;
    private CharSequence pkg;
    private CharSequence relativeName;

    public GeneratedResourceFile(
            JavaFileManager.Location location, CharSequence pkg, CharSequence relativeName) {
        this.location = location;
        this.pkg = pkg;
        this.relativeName = relativeName;
    }

    public JavaFileManager.Location getLocation() {
        return location;
    }

    public CharSequence getPkg() {
        return pkg;
    }

    public CharSequence getRelativeName() {
        return relativeName;
    }

    @Override
    public GeneratedFileType getType() {
        return GeneratedFileType.RESOURCE;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append(location.toString())
            .append(pkg.toString())
            .append(relativeName.toString())
            .toString();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + location.hashCode();
        result = 31 * result + pkg.hashCode();
        result = 31 * result + relativeName.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneratedResourceFile that = (GeneratedResourceFile) o;

        return location.equals(that.location) &&
            pkg.equals(that.pkg) &&
            relativeName.equals(that.relativeName);
    }
}
