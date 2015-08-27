package org.avaje.ebean.typequery.generator;

import org.avaje.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.avaje.ebean.typequery.generator.read.MetaReader;
import org.avaje.ebean.typequery.generator.write.SimpleManifestWriter;
import org.avaje.ebean.typequery.generator.write.SimpleQueryBeanWriter;
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
   *
   * <h3>Example usage:</h3>
   * <pre>{@code
   *
   *       GeneratorConfig config = new GeneratorConfig();
   *       config.setClassesDirectory("./target/test-classes");
   *       config.setEntityBeanPackage("org.example.domain");
   *       config.setDestDirectory("./src/test/java");
   *       config.setDestPackage("org.example.domain.query");
   *
   *       Generator generator = new Generator(config);
   *
   *       generator.generateQueryBeans();
   *
   * }</pre>
   */
  public void generateQueryBeans() throws IOException {

    generateQueryBeansOnly();
    generateManifest();
  }

  protected void generateQueryBeansOnly() throws IOException {

    MetaReader reader = new MetaReader(config.getClassesDirectory());
    reader.process(config.getEntityBeanPackage());

    generationMetaData.addAll(reader.getClassMetaData());

    Collection<EntityBeanPropertyReader> allEntities = generationMetaData.getAllEntities();

    for (EntityBeanPropertyReader classMeta : allEntities) {
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
    writer.write();
    writer.writeAssocBean();
  }

}
