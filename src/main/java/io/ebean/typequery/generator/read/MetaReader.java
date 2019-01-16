package io.ebean.typequery.generator.read;

import io.ebean.typequery.generator.GenerationMetaData;
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

  private final List<EntityBeanPropertyReader> classMetaData = new ArrayList<>();

  private final MetaClassFileReader classFileReader;

  private final String sourceDirectory;

  private final String filterEntityName;
  private final boolean filterPrefixMatch;

  private List<File> topDirectories = new ArrayList<>();

  public MetaReader(GenerationMetaData generationMetaData, String sourceDirectory, String filterEntityName) {
    this.sourceDirectory = trimSlash(sourceDirectory);
    this.classFileReader = new MetaClassFileReader(generationMetaData);
    if (filterEntityName == null || filterEntityName.isEmpty()) {
      this.filterEntityName = null;
      this.filterPrefixMatch = false;
    } else {
      this.filterPrefixMatch = filterPrefix(filterEntityName);
      this.filterEntityName = filterTrim(filterPrefixMatch, filterEntityName.toLowerCase());
    }
  }

  private boolean filterPrefix(String filterEntityName) {
    return filterEntityName.endsWith("*") || filterEntityName.endsWith("%");
  }

  private String filterTrim(boolean prefixMatch, String entityNameFilter) {
    if (prefixMatch) {
      return entityNameFilter.substring(0, entityNameFilter.length() - 1);
    } else {
      return entityNameFilter + ".class";
    }
  }

  /**
   * Return all meta data on all the collected classes.
   */
  public List<EntityBeanPropertyReader> getClassMetaData() {
    return classMetaData;
  }

  /**
   * Read a specific entity class files to read the meta data for.
   */
  public void processFiles(List<File> entityClassFiles) {
    for (File entityClassFile : entityClassFiles) {
      readClassMeta(entityClassFile);
    }
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
      processPackage("", true, true);
      return;
    }

    for (String pkgName : packageNames.split(",")) {

      String pkg = pkgName.trim().replace('.', '/');
      processPackage(trimSlash(pkg), true, true);
    }
  }


  public List<File> getTopDirectories() {
    return topDirectories;
  }

  private void processPackage(String dir, boolean recurse, boolean top) {

    String dirPath = sourceDirectory + "/" + dir;
    File directory = new File(dirPath);
    logger.debug("reading classes in {}", directory.getAbsolutePath());
    if (!directory.exists()) {
      File currentDir = new File(".");
      String m = "File not found " + dirPath + "  currentDir:" + currentDir.getAbsolutePath();
      throw new RuntimeException(m);
    }

    readDirectory(dir, recurse, directory.listFiles());

    if (top) {
      topDirectories.add(directory);
    }
  }

  private void readDirectory(String dir, boolean recurse, File[] files) {

    if (files == null) {
      return;
    }

    for (File file : files) {
      if (file.isDirectory()) {
        if (recurse) {
          processPackage(dir + "/" + file.getName(), true, false);
        }
      } else {

        if (file.getName().endsWith(".class")) {
          readClassMeta(file);

        } else {
          // possibly a common mistake... mixing .java and .class
          logger.debug("Expecting a .class file but got " + file.getAbsolutePath() + " ... ignoring");
        }
      }
    }
  }

  /**
   * Read the class metadata for the given className.
   * <p>
   * Typically used to load referenced MappedSuperclass metadata from different
   * packages or from the classpath.
   */
  public EntityBeanPropertyReader readViaClassPath(String superClassName) throws IOException {

    // first try the local file system
    String localClassFile = sourceDirectory + "/" + superClassName.replace('.', '/') + ".class";
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
      if (filterExclude(classFile)) {
        classMeta.setFilterExclude(true);
      }
      if (classMeta.isInterestingClass()) {
        logger.debug("read class meta data for {}", classMeta.name);
        classMetaData.add(classMeta);
      } else {
        logger.debug("... ignoring class {}", classMeta.name);
      }
    } catch (Exception e) {
      throw new RuntimeException("Error transforming file " + classFile.getAbsolutePath(), e);
    }
  }

  /**
   * Return true if this is an entity bean that should be excluded from generation.
   */
  private boolean filterExclude(File classFile) {
    if (filterEntityName == null || filterEntityName.isEmpty()) {
      return false;
    }
    String fileName = classFile.getName().toLowerCase();
    if (filterPrefixMatch) {
      return !fileName.startsWith(filterEntityName);
    } else {
      return !fileName.equals(filterEntityName);
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
