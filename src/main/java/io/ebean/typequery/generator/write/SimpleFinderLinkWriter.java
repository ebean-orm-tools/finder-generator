package io.ebean.typequery.generator.write;

import io.ebean.typequery.generator.GeneratorConfig;
import io.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes 'Finder' field into existing entity bean source code.
 */
public class SimpleFinderLinkWriter {

  protected static final Logger logger = LoggerFactory.getLogger(SimpleFinderLinkWriter.class);

  protected final GeneratorConfig config;

  protected final EntityBeanPropertyReader classMeta;

  protected final String entityBeanPackage;

  protected String finderPackage;

  protected String shortName;

  /**
   * Low tech, read all the source into this buffer and write it all out again.
   */
  protected List<String> existingSource = new ArrayList<>(300);

  /**
   * linux/unix new line but that is ok for now.
   */
  protected String newLine = "\n";

  public SimpleFinderLinkWriter(GeneratorConfig config, EntityBeanPropertyReader classMeta) {
    this.config = config;
    this.classMeta = classMeta;
    this.entityBeanPackage = config.getEntityBeanPackage();
    this.finderPackage = config.getDestFinderPackage();
    this.shortName = deriveShortName(classMeta.name);
  }

  /**
   * Write the find field into the entity bean if we don't think it is there.
   */
  public boolean write() throws IOException {

    File file = getEntitySourceFile();
    if (!file.exists()) {
      logger.warn("Could not find entity bean source java file {}", file.getAbsoluteFile());
      return false;
    }

    String finderDefn = config.lang().finderDefn(shortName);

    if (checkForExistingFinder(file, finderDefn)) {
      logger.debug("... existing find field on entity {}", shortName);
      return false;
    }

    String classDefn = "class " + shortName + " ";

    FileWriter writer = new FileWriter(file, false);

    boolean addedImport = false;
    boolean addedField = false;

    // loop the existing source and write it again including the
    // import and the finder field

    boolean javaLang = config.isJava();

    int size = existingSource.size();
    for (int i = 0; i < size; i++) {
      String sourceLine = existingSource.get(i);

      if (!addedImport && sourceLine.startsWith("import ")) {
        writer.append("import ").append(finderPackage).append(".").append(shortName).append("Finder");
        config.appendLangSemiColon(writer);
        writer.append(newLine);
        addedImport = true;
      }

      if (javaLang) {
        writer.append(sourceLine).append(newLine);
        if (!addedField && sourceLine.startsWith(classDefn)) {
          writer.append(newLine);
          writer.append("  public static final ").append(finderDefn).append(newLine);
          addedField = true;
        }
      } else {
        boolean atEnd = (size - i < 2);
        if (!addedField && atEnd && sourceLine.trim().equals("}")) {
          // Add Kotlin companion prior to last
          addedField = true;
          writer.append(newLine);
          writer.append("  ").append(finderDefn).append(newLine);
          writer.append(sourceLine).append(newLine);
        } else {
          writer.append(sourceLine).append(newLine);
        }
      }
    }

    if (!addedField && !javaLang) {
      // Kotlin with no final "}"
      writer.append(newLine);
      writer.append("{  ").append(finderDefn).append(newLine);
      writer.append("}").append(newLine);
    }

    writer.flush();
    writer.close();
    return true;
  }

  /**
   * Return true if the finder is already in the entity bean source file.
   */
  protected boolean checkForExistingFinder(File file, String finderDefn) throws IOException {
    FileReader reader = new FileReader(file);
    try {
      if (hasExistingFinder(reader, finderDefn)) {
        return true;
      }
    } finally {
      reader.close();
    }
    return false;
  }

  /**
   * Return true if the finder is already in the entity bean source reader.
   */
  protected boolean hasExistingFinder(Reader reader, String finderDefn) throws IOException {

    LineNumberReader lineReader = new LineNumberReader(reader);
    String line;
    while((line = lineReader.readLine()) != null) {
      if (line.contains(finderDefn)) {
        return true;
      }
      existingSource.add(line);
    }
    return false;
  }

  /**
   * Return the file for the entity bean source.
   */
  protected File getEntitySourceFile() throws IOException {

    String destDirectory = config.getDestDirectory();
    File destDir = new File(destDirectory);

    String packageAsDir = asSlashNotation(entityBeanPackage);

    File packageDir = new File(destDir, packageAsDir);

    String fileName = shortName + "." + config.getLang();
    return new File(packageDir, fileName);
  }

  protected String asSlashNotation(String path) {
    return path.replace('.', '/');
  }

  protected String deriveShortName(String name) {
    int startPos = name.lastIndexOf('/');
    if (startPos == -1) {
      return name;
    }
    return name.substring(startPos + 1);
  }

}
