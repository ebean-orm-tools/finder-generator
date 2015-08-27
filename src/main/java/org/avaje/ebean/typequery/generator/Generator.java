package org.avaje.ebean.typequery.generator;

import org.avaje.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.avaje.ebean.typequery.generator.read.MetaReader;
import org.avaje.ebean.typequery.generator.write.SimpleFinderLinkWriter;
import org.avaje.ebean.typequery.generator.write.SimpleFinderWriter;
import org.avaje.ebean.typequery.generator.write.SimpleManifestWriter;
import org.avaje.ebean.typequery.generator.write.SimpleQueryBeanWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
   * <h3>Example usage:</h3>
   * <pre>{@code
   *
   *       GeneratorConfig config = new GeneratorConfig();
   *       config.setEntityBeanPackage("org.example.domain");
   *
   *       Generator generator = new Generator(config);
   *       generator.generateQueryBeans();
   *
   * }</pre>
   *
   * <h3>Example usage:</h3>
   * <pre>{@code
   *
   *       GeneratorConfig config = new GeneratorConfig();
   *
   *       // specify directories when different from maven defaults
   *       config.setClassesDirectory("./target/test-classes");
   *       config.setDestDirectory("./src/test/java");
   *       config.setDestResourceDirectory("./src/test/resources");
   *
   *       config.setEntityBeanPackage("org.example.domain");
   *
   *       // specify a different destination package
   *       config.setDestPackage("org.example.domain.querybeans");
   *
   *       Generator generator = new Generator(config);
   *       generator.generateQueryBeans();
   *
   * }</pre>
   */
  public void generateQueryBeans() throws IOException {

    generateBeans();
    generateManifest();
  }

  /**
   * Generate 'finders'
   */
  public void generateFinders() throws IOException {

    for (EntityBeanPropertyReader classMeta : generationMetaData.getAllEntities()) {
      if (classMeta.isEntity()) {
        logger.info("generate finder for {}", classMeta.name);
        generateFinder(classMeta);
      }
    }
  }

  /**
   * For each of the entity beans add a finder field into them if they don't already have one.
   */
  public void modifyEntityBeansAddFinderField() throws IOException {
    for (EntityBeanPropertyReader classMeta : generationMetaData.getAllEntities()) {
      if (classMeta.isEntity()) {
        logger.info("link finder for {}", classMeta.name);
        linkFinder(classMeta);
      }
    }
  }

  protected void generateFinder(EntityBeanPropertyReader classMeta) throws IOException {
    SimpleFinderWriter writer = new SimpleFinderWriter(config, classMeta, generationMetaData);
    writer.write();
  }

  protected void linkFinder(EntityBeanPropertyReader classMeta) throws IOException {
    SimpleFinderLinkWriter writer = new SimpleFinderLinkWriter(config, classMeta);
    writer.write();
  }

  protected void generateBeans() throws IOException {

    MetaReader reader = new MetaReader(config.getClassesDirectory());
    reader.process(config.getEntityBeanPackage());

    generationMetaData.addAll(reader.getClassMetaData());

    for (EntityBeanPropertyReader classMeta : generationMetaData.getAllEntities()) {
      logger.info("generate for {}", classMeta.name);
      generateTypeQueryBeans(classMeta);
    }
  }

  protected void generateManifest() throws IOException {
    SimpleManifestWriter writer = new SimpleManifestWriter(config);
    writer.write();
  }

  protected void generateTypeQueryBeans(EntityBeanPropertyReader classMeta) throws IOException {

    // if is entity bean ...

    SimpleQueryBeanWriter writer = new SimpleQueryBeanWriter(config, classMeta, generationMetaData);
    writer.writeRootBean();
    writer.writeAssocBean();
  }

}
