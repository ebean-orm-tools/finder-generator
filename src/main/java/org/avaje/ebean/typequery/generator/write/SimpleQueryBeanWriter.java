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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple implementation that generates and writes query beans.
 */
public class SimpleQueryBeanWriter {

  protected static final Logger logger = LoggerFactory.getLogger(SimpleQueryBeanWriter.class);

  public static final String NEWLINE = "\n";

  protected final GeneratorConfig config;

  protected final EntityBeanPropertyReader classMeta;

  protected final GenerationMetaData generationMetaData;

  protected boolean writingAssocBean;

  protected String destPackage;
  protected String origDestPackage;

  protected String shortName;
  protected String origShortName;

  protected FileWriter writer;

  protected Set<String> importTypes = new TreeSet<>();

  protected List<PropertyMeta> properties = new ArrayList<>();

  public SimpleQueryBeanWriter(GeneratorConfig config, EntityBeanPropertyReader classMeta, GenerationMetaData generationMetaData) {
    this.config = config;
    this.classMeta = classMeta;
    this.generationMetaData = generationMetaData;

    destPackage = config.getDestPackage();
    shortName = deriveShortName(classMeta.name);
  }

  protected void gatherPropertyDetails() {

    importTypes.add(asDotNotation(classMeta.name));
    importTypes.add("org.avaje.ebean.typequery.TQRootBean");
    importTypes.add("org.avaje.ebean.typequery.TypeQueryBean");
    importTypes.add("com.avaje.ebean.EbeanServer");

    addClassProperties(classMeta);
  }

  /**
   * Recursively add properties from the inheritance hierarchy.
   * <p>
   * Includes properties from mapped super classes and usual inheritance.
   * </p>
   */
  protected void addClassProperties(EntityBeanPropertyReader classMetaData) {

    List<FieldNode> allProperties = classMetaData.getAllProperties(generationMetaData);
    for (FieldNode field : allProperties) {
      PropertyType type = generationMetaData.getPropertyType(field, classMeta);
      if (type == null) {
        logger.warn("No support for field [" + field.name + "] desc[" + field.desc + "] signature [" + field.signature + "]");
      } else {
        type.addImports(importTypes);
        properties.add(new PropertyMeta(field.name, type));
      }
    }
  }

  /**
   * Write the type query bean (root bean).
   */
  public void writeRootBean() throws IOException {

    gatherPropertyDetails();

    if (classMeta.isEntity()) {
      writer = createFileWriter();

      writePackage();
      writeImports();
      writeClass();
      writeAlias();
      writeFields();
      writeConstructors();
      writeClassEnd();

      writer.flush();
      writer.close();
    }
  }

  /**
   * Write the type query assoc bean.
   */
  public void writeAssocBean() throws IOException {

    writingAssocBean = true;
    origDestPackage = destPackage;
    destPackage = destPackage+".assoc";
    origShortName = shortName;
    shortName = "Assoc"+shortName;

    prepareAssocBeanImports();

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

  /**
   * Prepare the imports for writing assoc bean.
   */
  protected void prepareAssocBeanImports() {

    importTypes.remove(asDotNotation(classMeta.name));
    importTypes.remove("org.avaje.ebean.typequery.TQRootBean");
    importTypes.remove("com.avaje.ebean.EbeanServer");
    importTypes.add("org.avaje.ebean.typequery.TQAssocBean");
    importTypes.add(getEntityBeanImport());
    if (classMeta.isEntity()) {
      importTypes.add("org.avaje.ebean.typequery.TQProperty");
      importTypes.add(origDestPackage + ".Q" + origShortName);
    }

    if (!config.isAopStyle()) {
      importTypes.add("org.avaje.ebean.typequery.TQPath");
    }

    // remove imports for the same package
    Iterator<String> importsIterator = importTypes.iterator();
    while (importsIterator.hasNext()){
      String importType = importsIterator.next();
      // there are no subpackages so just use startsWith(destPackage)
      if (importType.startsWith(destPackage)) {
        importsIterator.remove();
      }
    }

  }

  private String getEntityBeanImport() {
    return classMeta.name.replaceAll("/", "\\.");
  }

  /**
   * Write constructors.
   */
  protected void writeConstructors() throws IOException {

    if (writingAssocBean) {
      writeAssocBeanFetch();
      writeAssocBeanConstructor();
    } else {
      writeRootBeanConstructor();
    }
  }

  /**
   * Write the constructors for 'root' type query bean.
   */
  protected void writeRootBeanConstructor() throws IOException {

    writer.append(NEWLINE);
    writer.append("  /**").append(NEWLINE);
    writer.append("   * Construct with a given EbeanServer.").append(NEWLINE);
    writer.append("   */").append(NEWLINE);
    writer.append("  public Q").append(shortName).append("(EbeanServer server) {").append(NEWLINE);
    writer.append("    super(").append(shortName).append(".class, server);").append(NEWLINE);
    writer.append("  }").append(NEWLINE);
    writer.append(NEWLINE);

    if (config.isAopStyle()) {
      writer.append("  /**").append(NEWLINE);
      writer.append("   * Construct using the default EbeanServer.").append(NEWLINE);
      writer.append("   */").append(NEWLINE);
      writer.append("  public Q").append(shortName).append("() {").append(NEWLINE);
      writer.append("    super(").append(shortName).append(".class);").append(NEWLINE);
      writer.append("  }").append(NEWLINE);

    } else {
      // verbose manual style requiring manual depth control (non-AOP)
      writer.append("  public Q").append(shortName).append("() {").append(NEWLINE);
      writer.append("    this(").append(String.valueOf(config.getMaxPathTraversalDepth())).append(");").append(NEWLINE);
      writer.append("  }").append(NEWLINE);

      writer.append("  public Q").append(shortName).append("(int maxDepth) {").append(NEWLINE);
      writer.append("    super(").append(shortName).append(".class);").append(NEWLINE);
      writer.append("    setRoot(this);").append(NEWLINE);

      for (PropertyMeta property : properties) {
        property.writeConstructorSimple(writer, shortName, false);
      }

      for (PropertyMeta property : properties) {
        property.writeConstructorAssoc(writer, shortName, false);
      }
      writer.append("  }").append(NEWLINE);
    }

    writer.append(NEWLINE);
    writer.append("  /**").append(NEWLINE);
    writer.append("   * Construct for Alias.").append(NEWLINE);
    writer.append("   */").append(NEWLINE);
    writer.append("  private Q").append(shortName).append("(boolean dummy) {").append(NEWLINE);
    writer.append("    super(dummy);").append(NEWLINE);
    writer.append("  }").append(NEWLINE);
  }

  protected void writeAssocBeanFetch() throws IOException {

    if (classMeta.isEntity()) {
      writer.append("  /**").append(NEWLINE);
      writer.append("   * Eagerly fetch this association loading the specified properties.").append(NEWLINE);
      writer.append("   */").append(NEWLINE);
      writer.append("  @SafeVarargs").append(NEWLINE);
      writer.append("  public final R fetch(TQProperty<Q").append(origShortName).append(">... properties) {").append(NEWLINE);
      writer.append("    return fetchProperties(properties);").append(NEWLINE);
      writer.append("  }").append(NEWLINE);
      writer.append(NEWLINE);
    }
  }

  /**
   * Write constructor for 'assoc' type query bean.
   */
  protected void writeAssocBeanConstructor() throws IOException {

    if (config.isAopStyle()) {
      // minimal constructor
      writer.append("  public Q").append(shortName).append("(String name, R root) {").append(NEWLINE);
      writer.append("    super(name, root);").append(NEWLINE);
      writer.append("  }").append(NEWLINE);

    } else {
      // generate the constructor for non-AOP manual/verbose style
      writer.append("  public Q").append(shortName).append("(String name, R root, int depth) {").append(NEWLINE);
      writer.append("    this(name, root, null, depth);").append(NEWLINE);
      writer.append("  }").append(NEWLINE);

      writer.append("  public Q").append(shortName).append("(String name, R root, String prefix, int depth) {").append(NEWLINE);
      writer.append("    super(name, root, prefix);").append(NEWLINE);
      writer.append("    String path = TQPath.add(prefix, name);").append(NEWLINE);
      for (PropertyMeta property : properties) {
        property.writeConstructorSimple(writer, shortName, true);
      }
      if (hasAssocProperties()) {
        writer.append("    if (--depth > 0) {").append(NEWLINE);
        for (PropertyMeta property : properties) {
          property.writeConstructorAssoc(writer, shortName, true);
        }
        writer.append("    }").append(NEWLINE);
      }
      writer.append("  }").append(NEWLINE);
    }
  }

  /**
   * Return true if this has at least one 'assoc' property.
   */
  protected boolean hasAssocProperties() {
    for (PropertyMeta property : properties) {
      if (property.isAssociation()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Write all the fields.
   */
  protected void writeFields() throws IOException {

    for (PropertyMeta property : properties) {
      property.writeFieldDefn(writer, shortName, writingAssocBean);
      writer.append(NEWLINE);
    }
    writer.append(NEWLINE);
  }

  /**
   * Write the class definition.
   */
  protected void writeClass() throws IOException {

    if (writingAssocBean) {
      writer.append("/**").append(NEWLINE);
      writer.append(" * Association query bean for ").append(shortName).append(".").append(NEWLINE);
      writer.append(" * ").append(NEWLINE);
      writer.append(" * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.").append(NEWLINE);
      writer.append(" */").append(NEWLINE);
      //public class QAssocContact<R>
      writer.append("@TypeQueryBean").append(NEWLINE);
      writer.append("public class ").append("Q").append(shortName);
      writer.append("<R> extends TQAssocBean<").append(origShortName).append(",R> {").append(NEWLINE);

    } else {
      writer.append("/**").append(NEWLINE);
      writer.append(" * Query bean for ").append(shortName).append(".").append(NEWLINE);
      writer.append(" * ").append(NEWLINE);
      writer.append(" * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.").append(NEWLINE);
      writer.append(" */").append(NEWLINE);
      //  public class QContact extends TQRootBean<Contact,QContact> {
      writer.append("@TypeQueryBean").append(NEWLINE);
      writer.append("public class ").append("Q").append(shortName)
          .append(" extends TQRootBean<").append(shortName).append(",Q").append(shortName).append("> {").append(NEWLINE);
    }

    writer.append(NEWLINE);
  }

  protected void writeAlias() throws IOException {
    if (!writingAssocBean) {
      writer.append("  private static final Q").append(shortName).append(" _alias = new Q");
      writer.append(shortName).append("(true);").append(NEWLINE);
      writer.append(NEWLINE);

      writer.append("  /**").append(NEWLINE);
      writer.append("   * Return the shared 'Alias' instance used to provide properties to ").append(NEWLINE);
      writer.append("   * <code>select()</code> and <code>fetch()</code> ").append(NEWLINE);
      writer.append("   */").append(NEWLINE);
      writer.append("  public static Q").append(shortName).append(" alias() {").append(NEWLINE);
      writer.append("    return _alias;").append(NEWLINE);
      writer.append("  }").append(NEWLINE);
      writer.append(NEWLINE);
    }
  }

  protected void writeClassEnd() throws IOException {
    writer.append("}").append(NEWLINE);
  }

  /**
   * Write all the imports.
   */
  protected void writeImports() throws IOException {

    for (String importType : importTypes) {
      writer.append("import ").append(importType).append(";").append(NEWLINE);
    }
    writer.append(NEWLINE);
  }

  protected void writePackage() throws IOException {
    writer.append("package ").append(destPackage).append(";").append(NEWLINE).append(NEWLINE);
  }


  protected FileWriter createFileWriter() throws IOException {

    String destDirectory = config.getDestDirectory();
    File destDir = new File(destDirectory);

    String packageAsDir = asSlashNotation(destPackage);

    File packageDir = new File(destDir, packageAsDir);
    if (!packageDir.exists() && !packageDir.mkdirs()) {
      logger.error("Failed to create directory [{}] for generated code", packageDir.getAbsoluteFile());
    }

    String fileName = "Q"+shortName+".java";
    File dest = new File(packageDir, fileName);

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
