package com.google.testing.compile;

import java.io.IOException;
import javax.tools.StandardLocation;

//this class is not currently used but could be useful to test the generated files
//at the API level.
public class InMemoryClassLoader extends ClassLoader {

  private InMemoryJavaFileManager fileManager;

  public InMemoryClassLoader(InMemoryJavaFileManager fileManager) {
    this.fileManager = fileManager;
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return super.loadClass(name);
  }

  @Override
  protected Class<?> findClass(String FQNclassName) throws ClassNotFoundException {
    int lastDotIndex = FQNclassName.lastIndexOf('.');
    String packageName = lastDotIndex == -1 ? "" : FQNclassName.substring(0, lastDotIndex);
    String className = FQNclassName.substring(lastDotIndex + 1);

    try {
      InMemoryJavaFileManager.InMemoryJavaFileObject fileForOutput =
          (InMemoryJavaFileManager.InMemoryJavaFileObject) fileManager.getFileForOutput(StandardLocation.CLASS_OUTPUT, packageName,
              className + ".class", null);
      if (fileForOutput != null) {
        byte[] array = fileForOutput.data.get().read();
        return defineClass(FQNclassName, array, 0, array.length);
      }
      return super.findClass(FQNclassName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
