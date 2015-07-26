package org.avaje.ebean.typequery.generator.write;


import org.avaje.ebean.typequery.generator.GenerationMetaData;
import org.avaje.ebean.typequery.generator.GeneratorConfig;
import org.avaje.ebean.typequery.generator.asm.tree.FieldNode;
import org.avaje.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 */
public class SimpleEntityBeanWriter {

  private static final Logger logger = LoggerFactory.getLogger(SimpleEntityBeanWriter.class);

  public static final String NEWLINE = "\n";

  private final GeneratorConfig config;

  private final EntityBeanPropertyReader classMeta;

  private final GenerationMetaData generationMetaData;

  private boolean writingAssocBean;

  String destPackage;

  String shortName;

  FileWriter writer;

  Set<String> importTypes = new TreeSet<>();

  List<PropertyMeta> properties = new ArrayList<>();

  public SimpleEntityBeanWriter(GeneratorConfig config, EntityBeanPropertyReader classMeta, GenerationMetaData generationMetaData) {
    this.config = config;
    this.classMeta = classMeta;
    this.generationMetaData = generationMetaData;

    destPackage = config.getDestPackage();
    shortName = deriveShortName(classMeta.name);
  }

  private void gatherPropertyDetails() {

    importTypes.add(asDotNotation(classMeta.name));
    importTypes.add("org.avaje.ebean.typequery.TQRootBean");

    List<FieldNode> fields = classMeta.fields;
    for (FieldNode field : fields) {
      if (includeField(field)) {
        PropertyType type = generationMetaData.getPropertyType(field, classMeta);
        if (type == null) {
          logger.warn("No support for field [" + field.name + "] desc[" + field.desc + "] signature [" + field.signature + "]");
        } else {
          type.addImports(importTypes);
          properties.add(new PropertyMeta(field.name, type));
        }
      }
    }
  }


  private boolean includeField(FieldNode field) {
    return true;
  }

  public void write() throws IOException {

    gatherPropertyDetails();

    writer = createFileWriter();

    writePackage();
    writeImports();
    writeClass();
    writeFields();
    writeConstructors();
    writeClassEnd();

    writer.flush();
    writer.close();
  }

  public void writeAssocBean() throws IOException {

    writingAssocBean = true;
    destPackage = destPackage+".assoc";
    shortName = "Assoc"+shortName;

    importTypes.remove(asDotNotation(classMeta.name));
    importTypes.remove("org.avaje.ebean.typequery.TQRootBean");
    importTypes.add("org.avaje.ebean.typequery.TQPath");

    writer = createFileWriter();

    writePackage();
    writeImports();
    writeClass();
    writeFields();
    writeConstructors();
    writeClassEnd();

    writer.flush();
    writer.close();
  }


  private void writeConstructors() throws IOException {

    if (writingAssocBean) {
      writeAssocBeanConstructor();
    } else {
      writeRootBeanConstructor();
    }
  }

  private void writeRootBeanConstructor() throws IOException {
    writer.append("  public Q" + shortName + "() {").append(NEWLINE);
    writer.append("    super(" + shortName + ".class);").append(NEWLINE);
    writer.append("    setRoot(this);").append(NEWLINE);

    for (PropertyMeta property : properties) {
      property.writeConstructorSimple(writer, shortName, false);
    }

    for (PropertyMeta property : properties) {
      property.writeConstructorAssoc(writer, shortName, false);
    }
    writer.append("  }").append(NEWLINE);
  }

  private void writeAssocBeanConstructor() throws IOException {
    writer.append("  public Q" + shortName + "(String name, R root, int depth) {").append(NEWLINE);
    writer.append("    this(name, root, null, depth);").append(NEWLINE);
    writer.append("  }").append(NEWLINE);

    writer.append("  public Q" + shortName + "(String name, R root, String prefix, int depth) {").append(NEWLINE);
    writer.append("    String path = TQPath.add(prefix, name);").append(NEWLINE);
    for (PropertyMeta property : properties) {
      property.writeConstructorSimple(writer, shortName, true);
    }
    if (hasAssocProperties()) {
      writer.append("    if (--depth > 0) {").append(NEWLINE);
      for (PropertyMeta property : properties) {
        property.writeConstructorAssoc(writer, shortName, true);
      }
    }
    writer.append("    }").append(NEWLINE);
    writer.append("  }").append(NEWLINE);
  }

  private boolean hasAssocProperties() {
    for (PropertyMeta property : properties) {
      if (property.isAssociation()) {
        return true;
      }
    }
    return false;
  }

  private void writeFields() throws IOException {

    for (PropertyMeta property : properties) {
      property.writeFieldDefn(writer, shortName, writingAssocBean);
      writer.append(NEWLINE);
    }
    writer.append(NEWLINE);
  }

  private void writeClass() throws IOException {

    if (writingAssocBean) {
      //public class QAssocContact<R>
      writer.append("public class ").append("Q").append(shortName).append("<R> {").append(NEWLINE);

    } else {
      //  public class QContact extends TQRootBean<Contact,QContact> {
      writer.append("public class ").append("Q").append(shortName)
          .append(" extends TQRootBean<" + shortName + ",Q" + shortName+"> {").append(NEWLINE);
    }

    writer.append(NEWLINE);
  }

  private void writeClassEnd() throws IOException {
    writer.append("}").append(NEWLINE);
  }

  private void writeImports() throws IOException {

    for (String importType : importTypes) {
      writer.append("import ").append(importType).append(";").append(NEWLINE);
    }
    writer.append(NEWLINE);
  }

  private void writePackage() throws IOException {
    writer.append("package ").append(destPackage).append(";").append(NEWLINE).append(NEWLINE);
  }


  private FileWriter createFileWriter() throws IOException {

    String destDirectory = config.getDestDirectory();
    File destDir = new File(destDirectory);

    String packageAsDir = asSlashNotation(destPackage);

    File packageDir = new File(destDir, packageAsDir);
    if (!packageDir.mkdirs()) {
      logger.error("Failed to create directories [{}]for generated code", packageDir.getAbsoluteFile());
    }

    String fileName = "Q"+shortName+".java";
    File dest = new File(packageDir, fileName);

    logger.info("writing {}", dest.getAbsolutePath());

    return new FileWriter(dest);
  }

  private String asDotNotation(String path) {
    return path.replace('/', '.');
  }

  private String asSlashNotation(String path) {
    return path.replace('.', '/');
  }

  private String deriveShortName(String name) {
    int startPos = name.lastIndexOf('/');
    if (startPos == -1) {
      return name;
    }
    return name.substring(startPos + 1);
  }

}
