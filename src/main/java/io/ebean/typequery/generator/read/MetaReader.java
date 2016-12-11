package io.ebean.typequery.generator.read;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Recursively reads the directories related to packages and reads the meta data
 * for the entity and embedded beans.
 */
public class MetaReader {

  private static final Logger logger = LoggerFactory.getLogger(MetaReader.class);

  private final MetaClassFileReader classFileReader = new MetaClassFileReader();

  private final List<EntityBeanPropertyReader> classMetaData = new ArrayList<>();

  private final String sourceDirectory;

  /**
   */
  public MetaReader(String sourceDirectory) {
    this.sourceDirectory = trimSlash(sourceDirectory);
  }

  /**
   * Return all meta data on all the collected classes.
   */
  public List<EntityBeanPropertyReader> getClassMetaData() {
    return classMetaData;
  }

  /**
   * Process all the comma delimited list of packages.
   * <p>
   * Package names are effectively converted into a directory on the file
   * system, and the class files are found and processed.
   * </p>
   */
  public void process(String packageNames) {

    if (packageNames == null) {
      processPackage("", true);
      return;
    }

    for (String pkgName : packageNames.split(",")) {

      String pkg = pkgName.trim().replace('.', '/');

      boolean recurse = false;
      if (pkg.endsWith("**")) {
        recurse = true;
        pkg = pkg.substring(0, pkg.length() - 2);

      } else if (pkg.endsWith("*")) {
        recurse = true;
        pkg = pkg.substring(0, pkg.length() - 1);
      }

      processPackage(trimSlash(pkg), recurse);
    }
  }


  private void processPackage(String dir, boolean recurse) {

    String dirPath = sourceDirectory + "/" + dir;
    File directory = new File(dirPath);
    if (!directory.exists()) {
      File currentDir = new File(".");
      String m = "File not found " + dirPath + "  currentDir:" + currentDir.getAbsolutePath();
      throw new RuntimeException(m);
    }

    readDirectory(dir, recurse, directory.listFiles());
  }

  private void readDirectory(String dir, boolean recurse, File[] files) {

    for (File file : files) {
      if (file.isDirectory()) {
        if (recurse) {
          processPackage(dir + "/" + file.getName(), true);
        }
      } else {

        if (file.getName().endsWith(".class")) {
          readClassMeta(file);

        } else {
          // possibly a common mistake... mixing .java and .class
          logger.warn("Expecting a .class file but got " + file.getAbsolutePath() + " ... ignoring");
        }
      }
    }
  }

  /**
   * Read the class metadata for the given className.
   *
   * Typically used to load referenced MappedSuperclass metadata from different
   * packages or from the classpath.
   */
  public EntityBeanPropertyReader readViaClassPath(String superClassName) throws IOException {

    // first try the local file system
    String localClassFile = sourceDirectory + "/" + superClassName.replace('.','/')+".class";
    File classFile = new File(localClassFile);
    if (classFile.exists()) {
      return classFileReader.readClassFile(classFile);
    }

    // load via classpath
    return classFileReader.readClassViaClassPath(superClassName);
  }

  /**
   * Read the raw class file getting the meta data (fields, annotations etc).
   */
  private void readClassMeta(File classFile) {
    try {
      EntityBeanPropertyReader classMeta = classFileReader.readClassFile(classFile);
      if (classMeta.isInterestingClass()) {
        logger.info("read class meta data for {}", classMeta.name);
        classMetaData.add(classMeta);
      } else {
        logger.debug("... ignoring class {}", classMeta.name);
      }
    } catch (Exception e) {
      throw new RuntimeException("Error transforming file " + classFile.getAbsolutePath(), e);
    }
  }

  /**
   * Trim off a trailing slash if required.
   */
  private String trimSlash(String dir) {
    if (dir.endsWith("/")) {
      return dir.substring(0, dir.length() - 1);
    } else {
      return dir;
    }
  }
}
