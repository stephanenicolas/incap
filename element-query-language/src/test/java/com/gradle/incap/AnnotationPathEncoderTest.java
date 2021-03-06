package com.gradle.incap;

import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static com.gradle.incap.util.TestUtil.encodeSuccessfully;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.gradle.incap.Annotation1;
import javax.tools.JavaFileObject;
import org.junit.Test;

public class AnnotationPathEncoderTest {

    private JavaFileObject source;
    private String expected;
    private String path;

    @Test
    public void testEncodeAnnotationOnField() {
        //GIVEN
        expected = "af:test.Test:foo:org.gradle.incap.Annotation1";
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
        path = encodeSuccessfully(source, Annotation1.class);

        //THEN
        assertThat(path, notNullValue());
        assertThat(path, is(expected));
    }
}
