package org.gradle.incap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Encapsulates information about an in-progress build.
 */
public class BuildSpec {

    public enum BuildType {FULL, INCREMENTAL}

    private BuildType buildType;
    private File workingDir;
    private List<File> classPath;
    private List<File> sourcePath;
    private Set<File> modifiedFiles;
    private Set<File> deletedFiles;

    private BuildSpec() {
    }

    /**
     * Returns the build type set for this build.
     */
    public BuildType getBuildType() {
        return buildType;
    }

    /**
     * Returns the incap working directory, specified by the build system.
     * @return the absolute path to the folder.  It may not exist if incap
     *         has not created any state yet.
     */
    public File getWorkingDir() {
        return workingDir;
    }

    /**
     * Returns the classpath set by the build system.
     * @return the immutable classpath
     */
    public List<File> getClassPath() {
        return classPath;
    }

    /**
     * Returns the source path, which is optionally set by the build system.
     * @return the immutable source path.  May be empty if not set.
     */
    public List<File> getSourcePath() {
        return sourcePath;
    }

    /**
     * For incremental build specs, this is the list of added and modified files.
     * @return an immutable list of the full paths to any modified (source) files.
     */
    public Set<File> getModifiedFiles() {
        return modifiedFiles;
    }

    /**
     * For incremental build specs, this is the list of deleted files.
     * @return an immutable list of the full paths to any deleted (source) files.
     */
    public Set<File> getDeletedFiles() {
        return deletedFiles;
    }

    /**
     * Builder class for {@code BuildSpec}.
     */
    public static class Builder {

        private BuildType buildType;
        private File workingDir;
        private List<File> classPath;
        private List<File> sourcePath;
        private Set<File> modifiedFiles;
        private Set<File> deletedFiles;

        /**
         * Sets the build type.  This is a required parameter.
         */
        public void setBuildType(BuildType type) {
            buildType = type;
        }

        /**
         * Returns the build type.  This is a required parameter.
         */
        public BuildType getBuildType() {
            return buildType;
        }

        /**
         * Sets the directory where Incap should persist (and look for) its state files.
         * @param absolutePath a folder, which need not exist (incap will create it)
         */
        public void setWorkingDir(File absolutePath) {
            workingDir = absolutePath;
        }

        /**
         * Sets the classpath.  This is a required parameter.
         * @param classPath - must not be null
         */
        public void setClassPath(List<File> classPath) {
            this.classPath = new ArrayList<>(classPath.size());
            this.classPath.addAll(classPath);
        }

        /**
         * Sets the sourcepath.  This is a required parameter.
         * @param sourcePath - must not be null
         */
        public void setSourcePath(List<File> sourcePath) {
            this.sourcePath = new ArrayList<>(sourcePath.size());
            this.sourcePath.addAll(sourcePath);
        }

        /**
         * Sets the modified files.  Optional parameter.
         * @param changed the changed files.  If specified, must not be null
         * but can be empty.
         */
        public void setModifiedFiles(Set<File> changed) {
            modifiedFiles = changed;
        }

        /**
         * Sets the deleted files for incremental builds.  Optional parameter.
         * @param deleted if specified, must not be null (but can be empty)
         */
        public void setDeletedFiles(Set<File> deleted) {
            deletedFiles = deleted;
        }

        /**
         * Builds the spec.
         *
         * @return the new, immutable spec
         * @throws IllegalArgumentException if any required argument was not provided
         */
        public BuildSpec build() {
            if (workingDir == null) {
                throw new IllegalArgumentException("working dir not provided");
            }
            if (buildType == null) {
                throw new IllegalArgumentException("build type not specified");
            }
            if (classPath == null) {
                throw new IllegalArgumentException("classpath not specified");
            }
            BuildSpec spec = new BuildSpec();

            spec.workingDir = workingDir;
            spec.buildType = buildType;

            spec.classPath = classPath != null ? classPath : Collections.EMPTY_LIST;
            spec.sourcePath = sourcePath != null ? sourcePath : Collections.EMPTY_LIST;

            spec.modifiedFiles = modifiedFiles != null
                    ? Collections.unmodifiableSet(modifiedFiles) : Collections.EMPTY_SET;

            spec.deletedFiles = deletedFiles != null
                    ? Collections.unmodifiableSet(deletedFiles) : Collections.EMPTY_SET;

            return spec;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
