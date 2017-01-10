package io.ebean.typequery.generator.write;

import io.ebean.typequery.generator.GenerationMetaData;
import io.ebean.typequery.generator.GeneratorConfig;
import io.ebean.typequery.generator.asm.Type;
import io.ebean.typequery.generator.asm.tree.FieldNode;
import io.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Writes 'Finder' java source code for an entity bean.
 */
public class SimpleFinderWriter {

  protected static final Logger logger = LoggerFactory.getLogger(SimpleFinderWriter.class);

  public static final String NEWLINE = "\n";

  protected final GeneratorConfig config;

  protected final EntityBeanPropertyReader classMeta;

  protected final GenerationMetaData generationMetaData;

  protected String finderPackage;

  protected String shortName;

  protected String idTypeShortName;

  protected FileWriter writer;

  protected Set<String> importTypes = new TreeSet<>();

  protected boolean addWhereMethod;

  protected boolean addTextMethod;

  protected boolean addPublicMethods;

  public SimpleFinderWriter(GeneratorConfig config, EntityBeanPropertyReader classMeta, GenerationMetaData generationMetaData) {
    this.config = config;
    this.classMeta = classMeta;
    this.generationMetaData = generationMetaData;
    this.finderPackage = config.getDestFinderPackage();
    this.addWhereMethod = config.isAddFinderWhereMethod();
    this.addTextMethod = config.isAddFinderTextMethod();
    this.addPublicMethods = config.isAddFinderWherePublic();
    this.shortName = deriveShortName(classMeta.name);
  }

  /**
   * Write the Finder.
   */
  public boolean write() throws IOException {

    File file = createFile();
    if (file.exists() && !config.isOverwriteExistingFinders()) {
      logger.debug("... skip existing finder - {}", file.getAbsoluteFile());
      return false;
    }

    FieldNode idProperty = classMeta.getIdProperty(generationMetaData);
    if (idProperty == null) {
      return false;
    }
    String typeDesc = idProperty.desc.substring(1, idProperty.desc.length() - 1);
    Type objectType = Type.getObjectType(typeDesc);
    String className = objectType.getClassName();
    if (!className.startsWith("java.lang.")) {
      importTypes.add(className);
    }
    idTypeShortName = getShortName(className);

    importTypes.add(asDotNotation(classMeta.name));
    importTypes.add("io.ebean.Finder");

    if (addWhereMethod) {
      String queryBean = config.getDestPackage()+".Q"+shortName;
      importTypes.add(queryBean);
    }

    writer = createFileWriter(file);
    writePackage();
    writeImports();
    writeClass();
    writeConstructors();
    writeMethods();
    writeClassEnd();
    writer.flush();
    writer.close();
    return true;
  }

  /**
   * Return the short name for the given class name.
   */
  private String getShortName(String className) {
    int pos = className.lastIndexOf('.');
    return pos == -1 ? className : className.substring(pos + 1);
  }

  /**
   * Write constructors.
   */
  protected void writeConstructors() throws IOException {

    config.lang().finderConstructors(writer, shortName);
  }

  /**
   * Potentially add a where() method for typed query building.
   */
  protected void writeMethods() throws IOException {

    String modifier = getModifier();
    if (addWhereMethod) {
      config.lang().finderWhere(writer, shortName, modifier);
      if (addTextMethod) {
        config.lang().finderText(writer, shortName, modifier);
      }
    }
  }

  /**
   * Return the protected or public modifier for extra methods.
   */
  protected String getModifier() {
    return addPublicMethods ? "public" : "protected";
  }

  /**
   * Write the class definition.
   */
  protected void writeClass() throws IOException {

    config.lang().finderClass(writer, shortName, idTypeShortName);
    writer.append(NEWLINE);
  }

  protected void writeClassEnd() throws IOException {
    writer.append("}").append(NEWLINE);
  }

  /**
   * Write all the imports.
   */
  protected void writeImports() throws IOException {

    for (String importType : importTypes) {
      writer.append("import ").append(importType);
      config.appendLangSemiColon(writer);
      writer.append(NEWLINE);
    }
    writer.append(NEWLINE);
  }

  protected void writePackage() throws IOException {
    writer.append("package ").append(finderPackage);
    config.appendLangSemiColon(writer);
    writer.append(NEWLINE).append(NEWLINE);
  }

  protected File createFile() throws IOException {

    String destDirectory = config.getDestDirectory();
    File destDir = new File(destDirectory);

    String packageAsDir = asSlashNotation(finderPackage);

    File packageDir = new File(destDir, packageAsDir);
    if (!packageDir.exists() && !packageDir.mkdirs()) {
      logger.error("Failed to create directory [{}] for generated code", packageDir.getAbsoluteFile());
    }

    String fileName = shortName + "Finder."+config.getLang();
    return new File(packageDir, fileName);
  }

  protected FileWriter createFileWriter(File dest) throws IOException {
    logger.info("writing {}", dest.getAbsolutePath());
    return new FileWriter(dest);
  }

  protected String asDotNotation(String path) {
    return path.replace('/', '.');
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
