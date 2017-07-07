package org.gradle.incap;

import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static org.junit.Assert.*;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaSourceSubjectFactory;
import javax.tools.JavaFileObject;
import org.junit.Test;

public class OneToOneAPTest {

  private JavaFileObject source;

  @Test
  public void testOneToOneAP_creates_GeneratedFile_WhenSingleFileToProcess() {
    //GIVEN
    //GIVEN
    source =
        forSourceString(
            "test.Test",
            "" //
                + "package test;\n" //
                + "import org.gradle.incap.Annotation1;\n" //
                + "public class Test {\n" //
                + "  @Annotation1 String foo;\n" //
                + "}");
    //WHEN
    //THEN
    Truth.assertAbout(JavaSourceSubjectFactory.javaSource())
        .that(source) //
        .withCompilerOptions("-Xlint:-processing") //
        .processedWith(new OneToOneAP()) //
        .compilesWithoutError();
  }

}