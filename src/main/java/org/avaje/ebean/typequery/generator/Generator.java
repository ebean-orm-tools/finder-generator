package org.avaje.ebean.typequery.generator;

import org.avaje.ebean.typequery.generator.asm.tree.AnnotationNode;
import org.avaje.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.avaje.ebean.typequery.generator.read.MetaReader;
import org.avaje.ebean.typequery.generator.write.SimpleEntityBeanWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Reads meta data on entity beans by reading the .class files and generates
 * code based on that.
 */
public class Generator {

  private static final Logger logger = LoggerFactory.getLogger(Generator.class);

  private final GeneratorConfig config;

  private List<EntityBeanPropertyReader> metaData;

  /**
   * Construct with configuration for reading and writing.
   */
  public Generator(GeneratorConfig config) {
    this.config = config;
  }

  /**
   * Process the code generation.
   */
  public void process() {

    MetaReader reader = new MetaReader(config.getSourceDirectory());
    reader.process(config.getSourcePackage());

    metaData = reader.getClassMetaData();

    for (EntityBeanPropertyReader classMeta : metaData) {
      if (ignore(classMeta)) {
        logger.info("ignore generate for {}", classMeta.name);

      } else {
        logger.info("generate for {}", classMeta.name);
        generateCode(classMeta);
      }
    }
  }

  private void generateCode(EntityBeanPropertyReader classMeta) {

    // if is embedded bean ...
    // if is entity bean ...
    generateCodeForEntityBean(classMeta);

  }

  private void generateCodeForEntityBean(EntityBeanPropertyReader classMeta) {

    // find extra inherited fields
    SimpleEntityBeanWriter writer = new SimpleEntityBeanWriter(config, classMeta);
    writer.write();

  }

  private boolean ignore(EntityBeanPropertyReader classMeta) {

    List<AnnotationNode> visibleAnnotations = classMeta.visibleAnnotations;
    if (visibleAnnotations == null) {
      return true;
    }
    for (AnnotationNode annotation : visibleAnnotations) {
      if (annotation.desc.equals("Ljavax/persistence/Entity;")) {
        return false;
      }
      if (annotation.desc.equals("Ljavax/persistence/Embedded;")) {
        return false;
      }
    }

    return true;
  }
}
