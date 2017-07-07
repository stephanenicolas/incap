package org.gradle.incap;

import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static org.junit.Assert.*;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaSourceSubjectFactory;
import javax.tools.JavaFileObject;
import org.junit.Test;

public class OneToOneAPTest {

  private JavaFileObject source;
  private JavaFileObject expected;

  @Test
  public void testOneToOneAP_creates_GeneratedFile_WhenSingleFileToProcess() {
    //GIVEN
    source =
        forSourceString(
            "test.Test",
            "" //
                + "package test;\n" //
                + "import org.gradle.incap.Annotation1;\n" //
                + "@Annotation1\n" //
                + "public class Test {\n" //
                + "}");
    expected =
        forSourceString(
            "OneToOneAP_TestGen0",
            "" //
                + "\n" //
                + "public class OneToOneAP_TestGen0 {\n" //
                + "}");

    //WHEN
    //THEN
    Truth.assertAbout(JavaSourceSubjectFactory.javaSource())
        .that(source) //
        .withCompilerOptions("-Xlint:-processing") //
        .processedWith(new OneToOneAP()) //
        .compilesWithoutError()
        .and()
        .generatesSources(expected);

  }

}