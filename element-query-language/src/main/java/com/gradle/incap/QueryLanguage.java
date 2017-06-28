package com.gradle.incap;

/**
 *
 *
 * <dl>
 *   <dt>annotations on classes
 *   <dd>{@code ac:className:annotationType}
 *   <dt>annotations on fields
 *   <dd>{@code af:className:fieldName:annotationType}
 *   <dt>annotations on methods
 *   <dd>{@code am:className:memberName:paramLength:paramType0:...paramTypeN:annotationType}
 *   <dt>annotations on constructors
 *   <dd>{@code am:className:init:paramLength:paramType0:...paramTypeN:annotationType}
 *   <dt>annotations on params
 *   <dd>{@code
 *       am:className:methodName:paramLength:paramType0:...paramTypeN:paramIndex:annotationType}
 * </dl>
 */
public interface QueryLanguage {
    String LOCATOR_ANNOTATION_ON_FIELD = "af";
    String SEPARATOR = ":";
}
