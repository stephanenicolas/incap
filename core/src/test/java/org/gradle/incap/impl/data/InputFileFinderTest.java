package org.gradle.incap.impl.data;

import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static org.gradle.incap.impl.data.util.TestUtil.findInputFileSuccessFully;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InputFileFinderTest {

  private JavaFileObject source;

  @Test
  public void inputFileFinder_should_returnClassContainingField() {
    //GIVEN
    source = forSourceString("test.Test", "" //
        + "package test;\n" //
        + "import com.example.Annotation1;\n" //
        + "public class Test {\n" //
        + "  @Annotation1 String foo;\n" //
        + "}");

    //WHEN
    InputFile inputFile = findInputFileSuccessFully(source);

    //THEN
    assertThat(inputFile, notNullValue());
    assertThat(inputFile.getName(), is("test.Test"));
  }

  @Test
  public void inputFileFinder_should_returnClassContainingMethod() {
    //GIVEN
    source = forSourceString("test.Test", "" //
        + "package test;\n" //
        + "import com.example.Annotation1;\n" //
        + "public class Test {\n" //
        + "  @Annotation1 void foo() {}\n" //
        + "}");

    //WHEN
    InputFile inputFile = findInputFileSuccessFully(source);

    //THEN
    assertThat(inputFile, notNullValue());
    assertThat(inputFile.getName(), is("test.Test"));
  }

  @Test
  public void inputFileFinder_should_returnClassItself() {
    //GIVEN
    source = forSourceString("test.Test", "" //
        + "package test;\n" //
        + "import com.example.Annotation1;\n" //
        + "@Annotation1\n" //
        + "public class Test {\n" //
        + "}");

    //WHEN
    InputFile inputFile = findInputFileSuccessFully(source);

    //THEN
    assertThat(inputFile, notNullValue());
    assertThat(inputFile.getName(), is("test.Test"));
  }

}
