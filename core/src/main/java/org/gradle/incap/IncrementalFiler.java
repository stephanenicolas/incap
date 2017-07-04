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
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/**
 * Replacement/proxy for the {@code javax.annotation.processing.Filer} passed by the non-incremental
 * processing toolchain (typically, {@code javac} or {@code apt}). Conformant Annotation Processors
 * (APs) must use the {@code IncrementalFiler} for all their file operations.
 *
 * <p>This works more or less identically to {@code Filer}, but for correct operation, the
 * Annotation Processor <em>must</em> supply one or more originating {@code Element}s for each
 * generated target file. They are not optional, and the API will throw a runtime exception if the
 * AP fails to provide them for a given output.
 */
public class IncrementalFiler implements Filer {

    private Filer filer;

    public IncrementalFiler(Filer filer) {
        this.filer = filer;
    }

    @Override
    public JavaFileObject createSourceFile(CharSequence name, Element... originatingElements)
            throws IOException {
        ProcessorWorkflow processorWorkflow = Incap.getProcessorWorkflow();
        processorWorkflow.getStateGraph().addSourceFile(name, originatingElements);
        return filer.createSourceFile(name, originatingElements);
    }

    @Override
    public JavaFileObject createClassFile(CharSequence name, Element... originatingElements)
            throws IOException {
        return filer.createClassFile(name, originatingElements);
    }

    @Override
    public FileObject createResource(
            JavaFileManager.Location location,
            CharSequence pkg,
            CharSequence relativeName,
            Element... originatingElements)
            throws IOException {
        return filer.createResource(location, pkg, relativeName, originatingElements);
    }

    @Override
    public FileObject getResource(
            JavaFileManager.Location location, CharSequence pkg, CharSequence relativeName)
            throws IOException {
        return filer.getResource(location, pkg, relativeName);
    }
}
