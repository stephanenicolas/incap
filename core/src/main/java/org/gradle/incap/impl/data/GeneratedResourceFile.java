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
}
