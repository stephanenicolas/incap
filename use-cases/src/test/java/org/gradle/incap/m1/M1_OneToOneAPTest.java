package org.gradle.incap.m1;

import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaSourceSubjectFactory;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import org.junit.Test;

import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static org.junit.Assert.fail;

public class M1_OneToOneAPTest {

  private JavaFileObject source;
  private JavaFileObject expected;

  @Test
  public void testM1_OneToOneAP_incremental_creates_GeneratedFile_WhenSingleFileToProcess_UsesPreviouslyCompiledClass() {
    //GIVEN
    source = forSourceString("test.Test", "" //
        + "package test;\n" //
        + "import org.gradle.incap.Annotation1;\n" //
        + "@Annotation1\n" //
        + "public class Test extends org.gradle.incap.util.Untouched {\n" //
        + "}");
    expected = forSourceString("M1_OneToOneAP_TestGen0", "" //
        + "\n" //
        + "public class M1_OneToOneAP_TestGen0 {\n" //
        + "}");

    //WHEN
    //THEN
    CompileTester.SuccessfulCompilationClause successfulCompilationClause = Truth.assertAbout(JavaSourceSubjectFactory.javaSource()) //
        .that(source) //
        .withCompilerOptions("-Xlint:-processing") //
        .withClasspathFrom(getClass().getClassLoader()) //
        .processedWith(new M1_OneToOneAP()) //
        .compilesWithoutError() //
        .and() //
        .generatesSources(expected);

    try {
      successfulCompilationClause //
          .and() //
          .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, "", "M1_OneToOneAP_UntouchedGen0");
      fail();
    } catch (AssertionError e) {
    }
  }
}