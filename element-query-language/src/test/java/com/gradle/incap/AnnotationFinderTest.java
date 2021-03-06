package com.gradle.incap;

import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static com.gradle.incap.util.TestUtil.querySuccessfully;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import org.junit.Test;

public class AnnotationFinderTest {

    private JavaFileObject source;
    private String query;
    private TypeElement element;

    @Test
    public void testQueryAnnotationOnField() {
        //GIVEN
        query = "af:test.Test:foo:org.gradle.incap.Annotation1";
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
        element = querySuccessfully(query, source);

        //THEN
        assertThat(element, notNullValue());
        assertThat(element.toString(), is("org.gradle.incap.Annotation1"));
    }
}
