package org.gradle.incap;

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
    private String workingDir;
    private List<String> classPath;
    private List<String> sourcePath;
    private Set<String> modifiedFiles;
    private Set<String> deletedFiles;

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
    public String getWorkingDir() {
        return workingDir;
    }

    /**
     * Returns the classpath set by the build system.
     * @return the immutable classpath
     */
    public List<String> getClassPath() {
        return classPath;
    }

    /**
     * Returns the source path, which is optionally set by the build system.
     * @return the immutable source path.  May be empty if not set.
     */
    public List<String> getSourcePath() {
        return sourcePath;
    }

    /**
     * For incremental build specs, this is the list of added and modified files.
     * @return an immutable list of the full paths to any modified (source) files.
     */
    public Set<String> getModifiedFiles() {
        return modifiedFiles;
    }

    /**
     * For incremental build specs, this is the list of deleted files.
     * @return an immutable list of the full paths to any deleted (source) files.
     */
    public Set<String> getDeletedFiles() {
        return deletedFiles;
    }

    /**
     * Builder class for {@code BuildSpec}.
     */
    public static class Builder {

        public class MissingArgumentException extends RuntimeException {

            public MissingArgumentException() {
            }

            public MissingArgumentException(String reason) {
                super(reason);
            }
        }

        private BuildType buildType;
        private String workingDir;
        private List<String> classPath;
        private List<String> sourcePath;
        private Set<String> modifiedFiles;
        private Set<String> deletedFiles;

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
        public void setWorkingDir(String absolutePath) {
            workingDir = absolutePath;
        }

        /**
         * Returns the absolute path to incap's working directory for state files.
         * This is a required attribute.
         * @return the default working directory
         */
        public String getWorkingDir() {
            return workingDir;
        }

        /**
         * Sets the classpath.  This is a required parameter.
         * @param classPath - must not be null
         */
        public void setClassPath(List<String> classPath) {
            this.classPath = new ArrayList<>(classPath.size());
            this.classPath.addAll(classPath);
        }

        /**
         * Returns the (mutable) classpath.
         * @return the classpath.
         */
        public List<String> getClassPath() {
            if (classPath == null) {
                return classPath = new ArrayList<>();
            }
            return classPath;
        }

        /**
         * Sets the sourcepath.  This is a required parameter.
         * @param sourcePath - must not be null
         */
        public void setSourcePath(List<String> sourcePath) {
            this.sourcePath = new ArrayList<>(sourcePath.size());
            this.sourcePath.addAll(sourcePath);
        }

        /**
         * Returns the (mutable) sourcepath.
         * @return the sourcepath, never null
         */
        public List<String> getSourcePath() {
            if (sourcePath == null) {
                return sourcePath = new ArrayList<>();
            }
            return sourcePath;
        }

        /**
         * Sets the modified files.  Optional parameter.
         * @param changed the changed files.  If specified, must not be null
         * but can be empty.
         */
        public void setModifiedFiles(Set<String> changed) {
            getModifiedFiles().addAll(modifiedFiles);
        }

        /**
         * Returns the (mutable) list of modified files added to the builder so far.
         * @return the list, which is created on demand if needed
         */
        public Set<String> getModifiedFiles() {
            if (modifiedFiles == null) {
                modifiedFiles = new HashSet<>();
            }
            return modifiedFiles;
        }

        /**
         * Sets the deleted files for incremental builds.  Optional parameter.
         * @param deleted if specified, must not be null (but can be empty)
         */
        public void setDeletedFiles(Set<String> deleted) {
            getDeletedFiles().addAll(deleted);
        }

        /**
         * Returns the (mutable) list of deleted files added to the builder so far.
         * @return the list, which is created on demand if needed
         */
        public Set<String> getDeletedFiles() {
            if (deletedFiles == null) {
                deletedFiles = new HashSet<>();
            }
            return deletedFiles;
        }

        /**
         * Builds the spec.
         *
         * @return the new, immutable spec
         * @throws MissingArgumentException if any required argument was not provided
         */
        public BuildSpec build() {
            if (workingDir == null) {
                throw new MissingArgumentException("working dir not provided");
            }
            if (buildType == null) {
                throw new MissingArgumentException("build type not specified");
            }
            if (classPath == null) {
                throw new MissingArgumentException("classpath not specified");
            }
            BuildSpec spec = new BuildSpec();

            spec.workingDir = workingDir;
            spec.buildType = buildType;

            spec.classPath = getClassPath();
            spec.sourcePath = getSourcePath();

            spec.modifiedFiles = Collections.unmodifiableSet(getModifiedFiles());
            spec.deletedFiles = Collections.unmodifiableSet(getDeletedFiles());

            return spec;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
