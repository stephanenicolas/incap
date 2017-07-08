package org.gradle.incap;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaSourceSubjectFactory;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.testing.compile.JavaFileObjects.forSourceString;

public class NRelatedToOneAPTest {

  @Test
  public void testNRelatedToOneAP_creates_GeneratedFiles_WhenSingleFileToProcess() {
    //GIVEN
    JavaFileObject source = forSourceString("test.Test", "" //
            + "package test;\n" //
            + "import org.gradle.incap.Annotation1;\n" //
            + "@Annotation1\n" //
            + "public class Test {\n" //
            + "}");
    JavaFileObject expected0 = forSourceString("NRelatedToOneAP_TestGen0", "" //
            + "\n" //
            + "public class NRelatedToOneAP_TestGen0 {\n" //
            + "}");

    //WHEN
    //THEN
    Truth.assertAbout(JavaSourceSubjectFactory.javaSource())
        .that(source) //
        .withCompilerOptions("-Xlint:-processing") //
        .processedWith(new NRelatedToOneAP()) //
        .compilesWithoutError()
        .and()
        .generatesSources(expected0);
  }

  @Test
  public void testNRelatedToOneAP_creates_GeneratedFiles_WhenTwoFileToProcess() {
    //GIVEN
    JavaFileObject source0 = forSourceString("test.Test0", "" //
        + "package test;\n" //
        + "import org.gradle.incap.Annotation1;\n" //
        + "@Annotation1\n" //
        + "public class Test0 {\n" //
        + "}\n" //
        + "@Annotation1\n" //
        + "class Test1 extends Test0{\n" //
        + "}");
    JavaFileObject expected00 = forSourceString("NRelatedToOneAP_Test0Gen0", "" //
        + "\n" //
        + "public class NRelatedToOneAP_Test0Gen0 {\n" //
        + "}");

    JavaFileObject expected01 = forSourceString("NRelatedToOneAP_Test1Gen0", "" //
        + "\n" //
        + "public class NRelatedToOneAP_Test1Gen0 {\n" //
        + "}");
    JavaFileObject expected10 = forSourceString("NRelatedToOneAP_Test1_Test0Gen0", "" //
        + "\n" //
        + "public class NRelatedToOneAP_Test1_Test0Gen0 {\n" //
        + "}");

    //WHEN
    //THEN
    Truth.assertAbout(JavaSourceSubjectFactory.javaSource())
        .that(source0) //
        .withCompilerOptions("-Xlint:-processing") //
        .processedWith(new NRelatedToOneAP()) //
        .compilesWithoutError()
        .and()
        .generatesSources(expected00, expected01, expected10);
  }

}