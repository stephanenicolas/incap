/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.incap;

import java.io.IOException;

/**
 * Encapsulates the INCAP workflow for conforming build systems. <p>
 *
 * Before invoking the java compiler, the build system should call {@link #preBuild}
 * with the appropriate information about the build, and inspect the result of the
 * call to determine how to proceed with the build.  If the build needs to be converted
 * from incremental to full-rebuild, the build system should call its {@code BuildWorkflow}
 * instance again with a full build spec. <p>
 * 
 * After the java compilation task (and its corresponding annotation processing) have
 * completed successfully, the build system should call {@link #postBuild} to notify
 * INCAP that the processing is finished, so that INCAP can persist its intermediate state.
 */
public interface BuildWorkflow {

    /**
     * Status returned by {@code preBuild}.
     */
    enum PreBuildResultStatus {
        /**
         * Incap has determined that the incremental build can proceed.
         */
        INCREMENTAL,

        /**
         * Incap cannot do incremental annotation processing for some reason; e.g.,
         * it has lost its persisted state graph.  Build system should fall back to a full
         * rebuild for correctness.  This means they should call {@code BuildWorkflow} again
         * for this build step, passing a new non-incremental {@code BuildSpec}.
         */
        FULL_REBUILD,

        /**
         * Incap has determined that the incremental change may proceed and does
         * not affect any annotation processors, so the build system has the option of
         * skipping the annotation processing; for instance, by passing {@code -proc:none}.
         */
        SKIP_AP,

        /**
         * Incap has encountered an unrecoverable error.  Build system should clean up and
         * fall back to a full build.
         */
        ERROR
    }

    /**
     * Result returned by {@code preBuild}.
     */
    interface PreBuildResult {
        /**
         * The status code returned for this build.
         */
        PreBuildResultStatus status();

        /**
         * If there was an error, a message to issue.
         * @return the build message, or null if none
         */
        String message();
    }

    /**
     * Status returned by {@code preBuild}.
     */
    enum PostBuildResultStatus {
        /**
         * Returned when incremental annotation processing was successful.
         * Any relevant state will have been persisted to disk.
         */
        SUCCESS,

        /**
         * Returned when incremental annotation processing failed for some reason.
         * Any intermediate state from this build step will have been discarded.
         */
        FAILURE
    }

    /**
     * Result returned by {@code preBuild}.
     */
    interface PostBuildResult {
        /**
         * Status code returned by {@code postBuild}.
         */
        PostBuildResultStatus status();

        /**
         * If there was an error/failure, the message to issue.
         * @return the build message, or null if none
         */
        String message();
    }

    /**
     * Build systems should call this method before invoking the java compiler.
     */
    PreBuildResult preBuild(BuildSpec spec);

    /**
     * Build systems should call this when the java compile task finishes.
     * <p>
     * Before returning from this call, Incap will attempt to persist its state:
     * <ul>
     *   <li>If the annotation processing was successful, Incap will merge its in-memory
     *       state into the persisted graph, and flush it to disk. </li>
     *   <li>If the annotation processing was unsuccessful, Incap will discard any
     *       in-memory state built up during this build step. </li>
     * </ul>
     * @return the build result
     * @throws IOException if the library was unable to persist its state
     */
    PostBuildResult postBuild() throws IOException;
}
