package org.gradle.incap.m2;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.IncrementalCompileTester;
import java.io.IOException;
import java.util.List;
import javax.tools.JavaFileObject;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.ImmutableList.of;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.InMemoryJavaFileManager.toBinaryName;
import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class M2_NRelatedToOneAPTest {

  JavaFileObject source0 = forSourceString("test.Test0", "" //
      + "package test;\n" //
      + "import org.gradle.incap.Annotation1;\n" //
      + "@Annotation1\n" //
      + "public class Test0 {\n" //
      + "}");

  JavaFileObject source1 = forSourceString("test.Test1", "" //
      + "package test;\n" //
      + "import org.gradle.incap.Annotation1;\n" //
      + "@Annotation1\n" //
      + "public class Test1 extends Test0 {\n" //
      + "}");

  JavaFileObject expected00 = forSourceString("M2_NRelatedToOneAP_Test0Gen0", "" //
      + "public class M2_NRelatedToOneAP_Test0Gen0 {\n" //
      + "}");

  JavaFileObject expected01 = forSourceString("M2_NRelatedToOneAP_Test1Gen0", "" //
      + "public class M2_NRelatedToOneAP_Test1Gen0 {\n" //
      + "}");
  JavaFileObject expected10 = forSourceString("M2_NRelatedToOneAP_Test1_Test0Gen0", "" //
      + "public class M2_NRelatedToOneAP_Test1_Test0Gen0 {\n" //
      + "}");

  ImmutableList<String> options = of("-Xlint:-processing");
  IncrementalCompileTester incrementalCompileTester;

  @Before
  public void setUp() throws Exception {
    incrementalCompileTester = new IncrementalCompileTester();
  }

  @Test
  public void testM2_NRelatedToOneAP_GeneratedFiles_whenIncrementalBuildOfSource1() throws ClassNotFoundException, IOException {
    //GIVEN
    Compilation compilation = incrementalCompileTester.compile(asList(source0, source1), createM2_NRelatedToOneAP(), options);

    for (JavaFileObject javaFileObject : asList(expected00, expected01, expected10)) {
      assertThat(compilation).generatedSourceFile(toBinaryName(javaFileObject)).hasSourceEquivalentTo(javaFileObject);
    }

    boolean allDeleted = incrementalCompileTester.deleteGeneratedFiles(expected00, expected01, expected10);
    assertThat(allDeleted, is(true));

    incrementalCompileTester.addToSourcePath(source0);

    //WHEN
    compilation = incrementalCompileTester.compile(asList(source1), createM2_NRelatedToOneAP(), options);

    //THEN
    assertThat(compilation).succeeded();

    for (JavaFileObject javaFileObject : asList(expected01, expected10)) {
      assertThat(compilation).generatedSourceFile(toBinaryName(javaFileObject)).hasSourceEquivalentTo(javaFileObject);
    }
  }

  //when M2 is implemented, remove this
  @Test(expected = AssertionError.class)
  public void testM2_NRelatedToOneAP_GeneratedFiles_whenIncrementalBuildOfSource0() throws ClassNotFoundException, IOException {
    //GIVEN
    Compilation compilation = incrementalCompileTester.fullBuild(asList(source0, source1), createM2_NRelatedToOneAP(), options);

    assertThat(compilation).succeeded();

    for (JavaFileObject javaFileObject : asList(expected00, expected01, expected10)) {
      assertThat(compilation).generatedSourceFile(toBinaryName(javaFileObject)).hasSourceEquivalentTo(javaFileObject);
    }

    //WHEN
    compilation = incrementalCompileTester.incrementalBuild(asList(source0), createM2_NRelatedToOneAP(), options);

    //THEN
    assertThat(compilation).succeeded();

    assertThat(compilation).generatedSourceFile(toBinaryName(expected00)).hasSourceEquivalentTo(expected00);

    //doesn't work but should once we have de-serialization of state graph working
    assertThat(compilation).generatedSourceFile(toBinaryName(expected10)).hasSourceEquivalentTo(expected10);
    //when M2 is implemented, remove this
    fail();
  }

  private List<M2_NRelatedToOneAP> createM2_NRelatedToOneAP() {
    return singletonList(new M2_NRelatedToOneAP());
  }
}