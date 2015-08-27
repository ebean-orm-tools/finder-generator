package org.avaje.ebean.typequery.generator.write;


import org.avaje.ebean.typequery.generator.GenerationMetaData;
import org.avaje.ebean.typequery.generator.GeneratorConfig;
import org.avaje.ebean.typequery.generator.asm.tree.AnnotationNode;
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

  public static final String TRANSIENT_ANNOTATION = "Ljavax/persistence/Transient;";

  public static final String NEWLINE = "\n";

  protected final GeneratorConfig config;

  protected final EntityBeanPropertyReader classMeta;

  protected final GenerationMetaData generationMetaData;

  protected boolean writingAssocBean;

  protected String destPackage;

  protected String shortName;

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

    String superClassName = asDotNotation(classMetaData.superName);
    if (!"java.lang.Object".equals(superClassName)) {
      // look for mappedSuperclass or inheritance etc
      EntityBeanPropertyReader superClass = generationMetaData.getSuperClass(superClassName);
      if (superClass != null) {
        logger.debug("... super type {}", superClassName);
        addClassProperties(superClass);
      }
    }
    addProperties(classMetaData.fields);
  }

  /**
   * Add the list of fields as properties on the query bean.
   */
  protected void addProperties(List<FieldNode> fields) {
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

  /**
   * Return true if the field should be included.
   */
  protected boolean includeField(FieldNode field) {
    // at the moment not filtering out transient fields?
    return persistentField(field);
  }

  protected boolean persistentField(FieldNode field) {

    // note transient modifier fields are already filtered out
    // along with static fields and ebean added fields

    if (field.visibleAnnotations != null) {
      // look for @Transient annotation
      for (AnnotationNode annotation : field.visibleAnnotations) {
        if (annotation.desc.equals(TRANSIENT_ANNOTATION)) {
          return false;
        }
      }
    }
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

  protected void prepareAssocBeanImports() {

    importTypes.remove(asDotNotation(classMeta.name));
    importTypes.remove("org.avaje.ebean.typequery.TQRootBean");
    importTypes.remove("com.avaje.ebean.EbeanServer");
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


  protected void writeConstructors() throws IOException {

    if (writingAssocBean) {
      writeAssocBeanConstructor();
    } else {
      writeRootBeanConstructor();
    }
  }

  protected void writeRootBeanConstructor() throws IOException {

    if (config.isAopStyle()) {
      writer.append("  /**").append(NEWLINE);
      writer.append("   * Construct using the default EbeanServer.").append(NEWLINE);
      writer.append("   */").append(NEWLINE);
      writer.append("  public Q").append(shortName).append("() {").append(NEWLINE);
      writer.append("    super(").append(shortName).append(".class);").append(NEWLINE);
      writer.append("  }").append(NEWLINE);
      writer.append(NEWLINE);
      writer.append("  /**").append(NEWLINE);
      writer.append("   * Construct with a given EbeanServer.").append(NEWLINE);
      writer.append("   */").append(NEWLINE);
      writer.append("  public Q").append(shortName).append("(EbeanServer server) {").append(NEWLINE);
      writer.append("    super(").append(shortName).append(".class, server);").append(NEWLINE);
      writer.append("  }").append(NEWLINE);

    } else {
      // verbose manual style requiring manual depth control (non-AOP)
      writer.append("  public Q").append(shortName).append("() {").append(NEWLINE);
      writer.append("    this(").append("" + config.getMaxPathTraversalDepth()).append(");").append(NEWLINE);
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
  }

  protected void writeAssocBeanConstructor() throws IOException {
    if (!config.isAopStyle()) {
      // only generate the constructor for non-AOP manual/verbose style
      writer.append("  public Q").append(shortName).append("(String name, R root, int depth) {").append(NEWLINE);
      writer.append("    this(name, root, null, depth);").append(NEWLINE);
      writer.append("  }").append(NEWLINE);

      writer.append("  public Q").append(shortName).append("(String name, R root, String prefix, int depth) {").append(NEWLINE);
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

  protected boolean hasAssocProperties() {
    for (PropertyMeta property : properties) {
      if (property.isAssociation()) {
        return true;
      }
    }
    return false;
  }

  protected void writeFields() throws IOException {

    for (PropertyMeta property : properties) {
      property.writeFieldDefn(writer, shortName, writingAssocBean);
      writer.append(NEWLINE);
    }
    writer.append(NEWLINE);
  }

  protected void writeClass() throws IOException {

    if (writingAssocBean) {
      //public class QAssocContact<R>
      writer.append("@TypeQueryBean").append(NEWLINE);
      writer.append("public class ").append("Q").append(shortName).append("<R> {").append(NEWLINE);

    } else {
      //  public class QContact extends TQRootBean<Contact,QContact> {
      writer.append("@TypeQueryBean").append(NEWLINE);
      writer.append("public class ").append("Q").append(shortName)
          .append(" extends TQRootBean<").append(shortName).append(",Q").append(shortName).append("> {").append(NEWLINE);
    }

    writer.append(NEWLINE);
  }

  protected void writeClassEnd() throws IOException {
    writer.append("}").append(NEWLINE);
  }

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
