package org.gradle.incap.impl.data.util;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaSourceSubjectFactory;
import javax.tools.JavaFileObject;
import org.gradle.incap.impl.data.InputFile;

public class TestUtil {

  public static InputFile findInputFileSuccessFully(JavaFileObject source) {
    InputFileFinderAnnotationTestProcessor processor = new InputFileFinderAnnotationTestProcessor();
    Truth.assertAbout(JavaSourceSubjectFactory.javaSource())
        .that(source) //
        .withCompilerOptions("-Xlint:-processing") //
        .processedWith(processor) //
        .compilesWithoutError();

    return processor.getInputFileForElement();
  }
}
