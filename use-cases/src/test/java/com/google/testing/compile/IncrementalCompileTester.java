package com.google.testing.compile;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import org.gradle.incap.Incap;
import org.gradle.incap.impl.ProccessorWorkFlowImpl;
import org.gradle.incap.impl.data.ElementEntry;
import org.gradle.incap.impl.data.GeneratedFile;
import org.gradle.incap.impl.data.GeneratedSourceFile;
import org.gradle.incap.impl.data.InputFile;
import org.gradle.incap.impl.data.StateGraph;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.testing.compile.JavaFileObjects.forSourceString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.tools.JavaFileObject.Kind.CLASS;
import static javax.tools.JavaFileObject.Kind.SOURCE;
import static javax.tools.StandardLocation.CLASS_OUTPUT;
import static javax.tools.StandardLocation.SOURCE_OUTPUT;

//many ideas come from http://atamur.blogspot.de/2009/10/using-built-in-javacompiler-with-custom.html
public class IncrementalCompileTester {

  InMemoryJavaFileManager fileManager;
  JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
  private DiagnosticCollector<JavaFileObject> diagnosticCollector;
  private StateGraph stateGraph;
  private Set<JavaFileObject> fullBuildSources;

  public IncrementalCompileTester() {
    reset();
  }

  public void reset() {
    this.fileManager = initFileManager();
    stateGraph = null;
  }

  public final Compilation compile(Iterable<? extends JavaFileObject> files, Iterable<? extends Processor> processors,
      ImmutableList<String> options) {
    JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, null, null, files);
    task.setProcessors(processors);
    boolean succeeded = task.call();
    Compiler internalCompiler = new AutoValue_Compiler(compiler, copyOf(processors), options);
    return new Compilation(internalCompiler, files, succeeded, diagnosticCollector.getDiagnostics(), getGeneratedFiles());
  }

  private ImmutableList<JavaFileObject> getGeneratedFiles() {
    return fileManager.getGeneratedSources();
  }

  public final Compilation fullBuild(Iterable<? extends JavaFileObject> files, Iterable<? extends Processor> processors,
      ImmutableList<String> options) {
    this.fullBuildSources = new HashSet<>();
    for (JavaFileObject file : files) {
      fullBuildSources.add(file);
    }

    Compilation compilation = compile(files, processors, options);
    stateGraph = Incap.getProcessorWorkflow().getStateGraph();
    return compilation;
  }

  public final Compilation incrementalBuild(Iterable<? extends JavaFileObject> files, Iterable<? extends Processor> processors,
      ImmutableList<String> options) {
    Set<JavaFileObject> incrementalSources = new HashSet<>();
    for (JavaFileObject file : files) {
      incrementalSources.add(file);
    }
    for (JavaFileObject file : fullBuildSources) {
      if (!incrementalSources.contains(file)) {
        addToSourcePath(file);
      } else {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.')).replace('/', '.');
        for (ElementEntry elementEntry : stateGraph.getMapInputToElements().get(new InputFile(name))) {
          Set<GeneratedFile> generatedFiles = stateGraph.getMapElementToGeneratedFiles().get(elementEntry);
          for (GeneratedFile generatedFile : generatedFiles) {
            if (generatedFile instanceof GeneratedSourceFile) {
              deleteGeneratedFiles(forSourceString(((GeneratedSourceFile) generatedFile).getName().toString(), ""));
            }
          }
        }
      }
    }
    Compilation compilation = compile(files, processors, options);
    this.stateGraph = Incap.getProcessorWorkflow().getStateGraph();
    return compilation;
  }

  public void addToSourcePath(JavaFileObject... sourceFiles) {
    fileManager.addToSourcePath(sourceFiles);
  }

  public InMemoryJavaFileManager.InMemoryJavaFileObject getGeneratedJavaFile(String FQNclassName) {
    try {
      int lastDotIndex = FQNclassName.lastIndexOf('.');
      String packageName = lastDotIndex == -1 ? "" : FQNclassName.substring(0, lastDotIndex);
      String className = FQNclassName.substring(lastDotIndex + 1);
      return (InMemoryJavaFileManager.InMemoryJavaFileObject) fileManager.getFileForOutput(SOURCE_OUTPUT, packageName, className + ".java", null);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public InMemoryJavaFileManager.InMemoryJavaFileObject getClassFile(String FQNclassName) {
    try {
      int lastDotIndex = FQNclassName.lastIndexOf('.');
      String packageName = lastDotIndex == -1 ? "" : FQNclassName.substring(0, lastDotIndex);
      String className = FQNclassName.substring(lastDotIndex + 1);
      return (InMemoryJavaFileManager.InMemoryJavaFileObject) fileManager.getFileForOutput(CLASS_OUTPUT, packageName, className + ".class", null);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private InMemoryJavaFileManager initFileManager() {
    diagnosticCollector = new DiagnosticCollector<>();
    return new InMemoryJavaFileManager(compiler.getStandardFileManager(diagnosticCollector, Locale.getDefault(), UTF_8));
  }

  public boolean deleteGeneratedFiles(JavaFileObject... generatedJavaFiles) {
    boolean allDeleted = true;
    for (JavaFileObject generatedJavaFile : generatedJavaFiles) {
      String name = generatedJavaFile.getName().substring(0, generatedJavaFile.getName().lastIndexOf('.'));
      allDeleted &= fileManager.deleteJavaFileObject(SOURCE_OUTPUT, name, SOURCE);
      allDeleted &= fileManager.deleteJavaFileObject(CLASS_OUTPUT, name, CLASS);
    }
    return allDeleted;
  }
}
