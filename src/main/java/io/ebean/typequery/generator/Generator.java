package io.ebean.typequery.generator;

import io.ebean.typequery.generator.read.EntityBeanPropertyReader;
import io.ebean.typequery.generator.read.MetaReader;
import io.ebean.typequery.generator.write.SimpleFinderLinkWriter;
import io.ebean.typequery.generator.write.SimpleFinderWriter;
import io.ebean.typequery.generator.write.SimpleQueryBeanWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Reads meta data on entity beans by reading the .class files and generates
 * code based on that.
 */
public class Generator {

  private static final Logger logger = LoggerFactory.getLogger(Generator.class);

  public static final String EBEAN_MODEL = "io.ebean.Model";

  private final GeneratorConfig config;

  private final GenerationMetaData generationMetaData;

  private boolean loadedMetaData;

  private int queryBeanCount;

  private Set<String> finders = new LinkedHashSet<>();

  private Set<String> finderLinks = new LinkedHashSet<>();

  /**
   * Construct with configuration for reading and writing.
   */
  public Generator(GeneratorConfig config) {
    this.config = config;
    this.generationMetaData = new GenerationMetaData(config);
  }

  /**
   * Return the set of entities loaded.
   */
  public Set<String> getLoadedEntities() {
    loadMetaData();
    return generationMetaData.getLoadedEntities();
  }

  /**
   * Return the number of query beans generated.
   */
  public int getQueryBeanCount() {
    return queryBeanCount;
  }

  /**
   * Return the set of finders generated.
   */
  public Set<String> getFinders() {
    return finders;
  }

  /**
   * Return the set of finders linked to entity beans.
   */
  public Set<String> getFinderLinks() {
    return finderLinks;
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
   *       // additionally generate finders
   *       generator.generateFinders();
   *       // additionally link the finders into existing entity beans
   *       generator.modifyEntityBeansAddFinderField();
   *
   * }</pre>
   * <p>
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
   *       // additionally generate finders
   *       generator.generateFinders();
   *
   *       // additionally link the finders into existing entity beans
   *       generator.modifyEntityBeansAddFinderField();
   *
   * }</pre>
   */
  public void generateQueryBeans() throws IOException {
    generateBeans();
  }

  /**
   * Generate 'finders'
   */
  public void generateFinders() throws IOException {

    loadMetaData();

    for (EntityBeanPropertyReader classMeta : generationMetaData.getAllEntities()) {
      if (classMeta.isEntity()) {
        generateFinder(classMeta);
      } else {
        logger.debug("... not an entity bean - no finder generated for {}", classMeta.name);
      }
    }
  }

  /**
   * For each of the entity beans add a finder field into them if they don't already have one.
   */
  public void modifyEntityBeansAddFinderField() throws IOException {

    loadMetaData();

    for (EntityBeanPropertyReader classMeta : generationMetaData.getAllEntities()) {
      if (classMeta.isEntity()) {
        linkFinder(classMeta);
      }
    }
  }

  protected void generateFinder(EntityBeanPropertyReader classMeta) throws IOException {
    SimpleFinderWriter writer = new SimpleFinderWriter(config, classMeta, generationMetaData);
    if (writer.write()) {
      logger.debug("... generated finder for {}", classMeta.name);
      finders.add(classMeta.name);
    }
  }

  protected void linkFinder(EntityBeanPropertyReader classMeta) throws IOException {
    SimpleFinderLinkWriter writer = new SimpleFinderLinkWriter(config, classMeta);
    if (writer.write()) {
      logger.debug("... added link to finder for {}", classMeta.name);
      finderLinks.add(classMeta.name);
    }
  }

  protected void loadMetaData() {

    if (!loadedMetaData) {
      MetaReader reader = new MetaReader(config.getClassesDirectory());
      List<File> entityClassFiles = config.getEntityClassFiles();
      if (entityClassFiles != null) {
        reader.processFiles(entityClassFiles);
      } else {
        reader.process(config.getEntityBeanPackage());
      }

      generationMetaData.addAll(reader.getClassMetaData());

      // load any MappedSuperclass that are in different packages etc
      for (EntityBeanPropertyReader classMeta : generationMetaData.getAllEntities()) {
        if (!classMeta.isEnum()) {
          loadMappedSuper(classMeta, reader);
        }
      }

      if (generationMetaData.isEmpty()) {
        logger.warn("Didn't load any metadata? " + reader.getTopDirectories());
      } else {
        logger.debug("... loaded metadata for {}", generationMetaData.getDescription());
      }

      loadedMetaData = true;
    }
  }

  protected void generateBeans() throws IOException {

    loadMetaData();
    // actually generate the type query bean source
    for (EntityBeanPropertyReader classMeta : generationMetaData.getAllEntities()) {
      logger.info("generate for {}", classMeta.name);
      generateTypeQueryBeans(classMeta);
    }
  }

  /**
   * Load the class metadata for MappedSuperclass that have not already been loaded.
   */
  private void loadMappedSuper(EntityBeanPropertyReader classMeta, MetaReader reader) {
    String superClassName = asDotNotation(classMeta.getSuperClass());
    if (!"java.lang.Object".equals(superClassName) && !"io.ebean.Model".equals(superClassName)) {
      EntityBeanPropertyReader superClass = generationMetaData.getSuperClass(superClassName);
      if (superClass == null) {
        superClass = readViaClassPath(superClassName, reader);
      }
      if (superClass != null) {
        loadMappedSuper(superClass, reader);
      }
    }
  }

  /**
   * Load the class metadata for a given mapped superclass class.
   */
  private EntityBeanPropertyReader readViaClassPath(String superClassName, MetaReader reader) {

    try {
      if (superClassName.equals(EBEAN_MODEL)) {
        return null;
      }
      EntityBeanPropertyReader classMeta = reader.readViaClassPath(superClassName);
      generationMetaData.addClassMeta(classMeta);
      return classMeta;

    } catch (IOException e) {
      logger.error("Error trying to read class metadata for " + superClassName, e);
      return null;
    }
  }

  protected String asDotNotation(String path) {
    return path.replace('/', '.');
  }

  protected void generateTypeQueryBeans(EntityBeanPropertyReader classMeta) throws IOException {

    // if is entity bean ...

    SimpleQueryBeanWriter writer = new SimpleQueryBeanWriter(config, classMeta, generationMetaData);
    writer.writeRootBean();
    writer.writeAssocBean();
    queryBeanCount++;
  }

}
