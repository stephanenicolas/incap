package org.gradle.incap.m1;

import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaSourceSubjectFactory;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import org.junit.Test;

import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static org.junit.Assert.fail;

public class M1_NRelatedToOneAPTest {

  @Test
  public void testM1_NRelatedToOneAP_creates_GeneratedFiles_WhenSingleFileToProcess_UsesPreviouslyCompiledClass() {
    //GIVEN
    JavaFileObject source = forSourceString("test.Test", "" //
        + "package test;\n" //
        + "import org.gradle.incap.Annotation1;\n" //
        + "@Annotation1\n" //
        + "public class Test extends org.gradle.incap.util.Untouched{\n" //
        + "}");
    JavaFileObject expected0 = forSourceString("M1_NRelatedToOneAP_TestGen0", "" //
        + "\n" //
        + "public class M1_NRelatedToOneAP_TestGen0 {\n" //
        + "}");

    //WHEN
    //THEN
    CompileTester.SuccessfulCompilationClause successfulCompilationClause = Truth.assertAbout(JavaSourceSubjectFactory.javaSource()).that(source) //
        .withCompilerOptions("-Xlint:-processing") //
        .processedWith(new M1_NRelatedToOneAP()) //
        .compilesWithoutError().and().generatesSources(expected0);

    try {
      successfulCompilationClause //
          .and() //
          .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, "", "M1_NRelatedToOneAP_UntouchedGen0");
      fail();
    } catch (AssertionError e) {
    }
  }

  @Test
  public void testM1_NRelatedToOneAP_creates_GeneratedFiles_WhenTwoFileToProcess_UsesPreviouslyCompiledClass() {
    //GIVEN
    JavaFileObject source0 = forSourceString("test.Test0", "" //
        + "package test;\n" //
        + "import org.gradle.incap.Annotation1;\n" //
        + "@Annotation1\n" //
        + "public class Test0 extends org.gradle.incap.util.Untouched {\n" //
        + "}\n" //
        + "@Annotation1\n" //
        + "class Test1 extends Test0{\n" //
        + "}");
    JavaFileObject expected00 = forSourceString("M1_NRelatedToOneAP_Test0Gen0", "" //
        + "\n" //
        + "public class M1_NRelatedToOneAP_Test0Gen0 {\n" //
        + "}");

    JavaFileObject expected01 = forSourceString("M1_NRelatedToOneAP_Test1Gen0", "" //
        + "\n" //
        + "public class M1_NRelatedToOneAP_Test1Gen0 {\n" //
        + "}");
    JavaFileObject expected10 = forSourceString("M1_NRelatedToOneAP_Test1_Test0Gen0", "" //
        + "\n" //
        + "public class M1_NRelatedToOneAP_Test1_Test0Gen0 {\n" //
        + "}");

    //WHEN
    //THEN
    CompileTester.SuccessfulCompilationClause successfulCompilationClause = Truth.assertAbout(JavaSourceSubjectFactory.javaSource()).that(source0) //
        .withCompilerOptions("-Xlint:-processing") //
        .processedWith(new M1_NRelatedToOneAP()) //
        .compilesWithoutError().and().generatesSources(expected00, expected01, expected10);

    try {
      Truth.assertAbout(JavaSourceSubjectFactory.javaSource()).that(source0) //
          .withCompilerOptions("-Xlint:-processing") //
          .processedWith(new M1_NRelatedToOneAP()) //
          .compilesWithoutError().and().generatesSources(expected00, expected01, expected10) //
          .and() //
          .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, "", "M1_NRelatedToOneAP_UntouchedGen0");
      fail();
    } catch (AssertionError e) {
    }
  }
}