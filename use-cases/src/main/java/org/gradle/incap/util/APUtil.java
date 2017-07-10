package org.gradle.incap.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

import static java.lang.String.format;

public class APUtil {

  private APUtil() {
  }

  public static String getEnclosingClassName(Element elementWithAnnotation) {
    return elementWithAnnotation.getSimpleName().toString();
  }

  private static String brewJava(String targetClassName) {
    return new StringBuilder() //
        .append(format("public class %s {\n", targetClassName)) //
        .append("}") //
        .toString();
  }

  public static void generateFiles(Filer filer, Map<String, Set<? extends Element>> mapGeneratedFileNameToOriginatingElements) {
    for (Map.Entry<String, Set<? extends Element>> entryFileNameToOriginatingElements : mapGeneratedFileNameToOriginatingElements.entrySet()) {
      String generatedFileName = entryFileNameToOriginatingElements.getKey();
      Set<? extends Element> originatingElements = entryFileNameToOriginatingElements.getValue();
      generateFile(filer, generatedFileName, originatingElements);
    }
  }

  private static void generateFile(Filer filer, String targetClassName, Set<? extends Element> originatingElements) {
    PrintWriter printWriter = null;
    try {
      JavaFileObject generatedObjectFile = filer.createSourceFile(targetClassName, originatingElements.toArray(new Element[0]));
      Writer writer = generatedObjectFile.openWriter();
      printWriter = new PrintWriter(writer);
      String javaString = APUtil.brewJava(targetClassName);
      printWriter.append(javaString);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (printWriter != null) {
        printWriter.close();
      }
    }
  }
}
