package org.gradle.incap.m1;

import com.google.testing.compile.CompileTester.SuccessfulCompilationClause;
import com.google.testing.compile.JavaSourceSubjectFactory;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static org.junit.Assert.fail;

public class M1_OneToNAPTest {

  @Test
  public void testM1_OneToNAP_incremental_creates_GeneratedFiles_WhenSingleFileToProcess_UsesPreviouslyCompiledClass() {
    //GIVEN
    JavaFileObject source = forSourceString("test.Test", "" //
        + "package test;\n" //
        + "import org.gradle.incap.Annotation1;\n" //
        + "@Annotation1\n" //
        + "public class Test extends org.gradle.incap.util.Untouched {\n" //
        + "}");
    JavaFileObject expected0 = forSourceString("M1_OneToNAP_TestGen0", "" //
        + "\n" //
        + "public class M1_OneToNAP_TestGen0 {\n" //
        + "}");

    JavaFileObject expected1 = forSourceString("M1_OneToNAP_TestGen1", "" //
        + "\n" //
        + "public class M1_OneToNAP_TestGen1 {\n" //
        + "}");

    //WHEN
    //THEN
    SuccessfulCompilationClause successfulCompilationClause = assertAbout(JavaSourceSubjectFactory.javaSource()).that(source) //
        .withCompilerOptions("-Xlint:-processing") //
        .withClasspathFrom(getClass().getClassLoader()) //
        .processedWith(new M1_OneToNAP()) //
        .compilesWithoutError() //
        .and() //
        .generatesSources(expected0, expected1);

    try {
      successfulCompilationClause //
          .and() //
          .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, "", "M1_OneToNAP_UntouchedGen0");
      fail();
    } catch (AssertionError e) {
    }
  }
}