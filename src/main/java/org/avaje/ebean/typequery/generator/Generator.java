package org.avaje.ebean.typequery.generator;

import org.avaje.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.avaje.ebean.typequery.generator.read.MetaReader;
import org.avaje.ebean.typequery.generator.write.SimpleEntityBeanWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

/**
 * Reads meta data on entity beans by reading the .class files and generates
 * code based on that.
 */
public class Generator {

  private static final Logger logger = LoggerFactory.getLogger(Generator.class);

  private final GeneratorConfig config;

  private final GenerationMetaData generationMetaData;

  /**
   * Construct with configuration for reading and writing.
   */
  public Generator(GeneratorConfig config) {
    this.config = config;
    this.generationMetaData = new GenerationMetaData(config);
  }

  /**
   * Process the code generation.
   */
  public void process() throws IOException {

    MetaReader reader = new MetaReader(config.getSourceDirectory());
    reader.process(config.getSourcePackage());

    generationMetaData.addAll(reader.getClassMetaData());

    Collection<EntityBeanPropertyReader> allEntities = generationMetaData.getAllEntities();

    for (EntityBeanPropertyReader classMeta : allEntities) {
      logger.info("generate for {}", classMeta.name);
      generateRootQueryBeans(classMeta);
    }

    for (EntityBeanPropertyReader classMeta : allEntities) {
      logger.info("generate for {}", classMeta.name);
      generateAssocBeans(classMeta);
    }
  }

  private void generateAssocBeans(EntityBeanPropertyReader classMeta) {

  }

  private void generateRootQueryBeans(EntityBeanPropertyReader classMeta) throws IOException {

    // if is entity bean ...

    // find extra inherited fields
    SimpleEntityBeanWriter writer = new SimpleEntityBeanWriter(config, classMeta, generationMetaData);
    writer.write();
    writer.writeAssocBean();
  }

}
