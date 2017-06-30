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

import java.io.File;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.gradle.incap.impl.data.StateGraph;

/**
 * Main INCAP API surface for annotation processors. <p>
 *
 * INCAP arranges with the build system (via the {@link BuildWorkflow}
 * interface) to keep track of the state of added, modified and deleted files
 * and their corresponding {@code Element}s. <p>
 *
 * In order to become INCAP-compliant, an AP must modify its current processing
 * workflow the following ways: <p>
 *
 * <ol>
 *   <li>It should procure an instance of {@code ProcessorWorkflow}.</li>
 *
 *   <li>On the enclosing tool environment's call to {@code Processor.init()},
 *       the AP should pass the arguments to the {@code ProcessorWorkflow#process},
 *       and save the returned {@link IncrementalFiler}.</li>
 *
 *   <li>On the enclosing tool environment's call to {@code Processor.process()},
 *       the AP should pass the arguments to {@link ProcessorWorkflow#process}</li>
 *
 *   <li>The AP should use the {@code IncrementalFiler} for creating files.
 *       The only difference in usage is that passing {@code originatingElements}
 *       to the file operations is mandatory, and will throw an exception if at least
 *       one originating element is not passed to the incremental filer.</li>
 *
 *   <li>Before generating any output file (target), regardless of whether it is
 *       a source file, a class file, or a resource file, the AP must call
 *       {@link #getParticipatingElements}.  The AP should combine any {@code Element}s
 *       returned by this call, with elements discovered from the inputs to
 *       {@code Processor.process()}, to produce the full set of originating
 *       {@code Element}s for that output file.
 *   </li>
 *
 * Note that the workflow is largely identical for both full and incremental
 * builds.  For full builds, {@code getParticipatingElements} will return an empty
 * set, because there are no originating elements from previous builds. APs may
 * choose to do conditional work depending on whether the build is full or incremental,
 * by calling {@link #isIncremental}, but this is optional and dependent on the AP. <p>
 *
 * An AP that adjusts its workflow according to this specification should
 * see correct operation in incremental build contexts.
 * </ol>
 */
public interface ProcessorWorkflow {

    /**
     * Returns {@code true} if this build was flagged as incremental by the build system.
     */
    boolean isIncremental();

    /**
     * APs should pass in the {@code ProcessingEnvironment} passed to their
     * {@code Processor.init} method.
     *
     * @return a replacement for the {@code javax.annotation.processing.Filer}
     * passed to the {@code ProcessingEnvironment}.  APs should use the
     * {@code IncrementalFiler} for all file operations, even on full builds.
     */
    IncrementalFiler init(ProcessingEnvironment processingEnv);

    /**
     * APs should begin their {@code process()} method by passing its inputs to
     * an instance of {@code ProcessorWorkflow}.  They may then proceed normally
     * with annotation processing, with the following caveats:
     * <ul>
     *   <li>They must use the {@link IncrementalFiler} for creating files.</li>
     *   <li>They must call {@link getParticipatingElements} before generating files.</li>
     * </ul>
     */
    void process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    /**
     * Returns the set of non-modified {@code Element}s which are marked (from
     * previous builds) as originating elements in the generation of {@code target}.
     * This is the fundamental mechanism by which APs may support incrementality. <p>
     *
     * This call queries the Provenance Graph maintained between builds by INCAP.
     * The provenance graph keeps track of which elements participate in generating
     * outputs for each AP.  It is expected that the AP should merge (union) the
     * elements from this call with any modified elements passed to its {@code process()}
     * method, to produce the full set of originating elements for generating {@code target}.
     * <p>
     * That is to say, {@code originatingElements = modifiedElements + participatingElements}.
     */
    Set<Element> getParticipatingElements(File target);

    StateGraph getStateGraph();
}
