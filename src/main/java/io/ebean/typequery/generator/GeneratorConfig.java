package io.ebean.typequery.generator;

import io.ebean.typequery.generator.write.JavaLangAdapter;
import io.ebean.typequery.generator.write.KotlinLangAdapter;
import io.ebean.typequery.generator.write.LangAdapter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

/**
 * Configuration for the code generation.
 * <p>
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
public class GeneratorConfig {

  private String lang = "java";

  /**
   * The directory where the compiled classes are found.
   * Default to maven target/classes.
   */
  private String classesDirectory = "target/classes";

  private String entityBeanPackage;

  private List<File> entityClassFiles;

  private String destDirectory = "src/main/java";

  private String destResourceDirectory = "src/main/resources";

  private String destPackage;

  private String destAssocPackage;

  private boolean addFinderWhereMethod = true;

  private boolean addFinderTextMethod = true;

  private boolean addFinderWherePublic;

  private boolean overwriteExistingFinders;

  private boolean aopStyle = true;

  /**
   * The maximum path depth allowed in the generated code (non AOP manual style).
   */
  private int maxPathTraversalDepth = 4;

  private LangAdapter languageAdapter = new JavaLangAdapter();

  /**
   * Filter the entities to generate finders for based on this.
   */
  private String entityNameFilter;


  public String getEntityNameFilter() {
    return entityNameFilter;
  }

  /**
   * Set a filter to limit the entities we generate finders for.
   */
  public void setEntityNameFilter(String entityNameFilter) {
    this.entityNameFilter = entityNameFilter;
  }

  /**
   * Return the language.
   */
  public String getLang() {
    return lang;
  }

  /**
   * Set the language;
   */
  public void setLang(String lang) {
    this.lang = lang;
    this.languageAdapter = new KotlinLangAdapter();
  }

  /**
   * Return true if we are generating Java.
   */
  public boolean isJava() {
    return "java".equals(lang);
  }

  /**
   * Return true if we are generating Kotlin.
   */
  public boolean isKotlin() {
    return "kt".equals(lang);
  }

  /**
   * Return the directory path to the compiled classes.
   */
  public String getClassesDirectory() {
    return classesDirectory;
  }

  /**
   * Set the directory path to the compiled classes.
   * <p>
   * The compiled classes are read for the entity beans to gather the meta data (properties etc)
   * used to generate the query beans.
   * <p>
   * <pre>{@code
   *
   *       GeneratorConfig config = new GeneratorConfig();
   *       config.setClassesDirectory("./target/classes");
   *       config.setEntityBeanPackage("org.example.domain");
   *       ...
   *
   * }</pre>
   */
  public void setClassesDirectory(String classesDirectory) {
    this.classesDirectory = classesDirectory;
  }

  /**
   * Return the package name containing the entity beans.
   */
  public String getEntityBeanPackage() {
    return entityBeanPackage;
  }

  /**
   * Set the package name containing the entity beans.
   * <p>
   * <pre>{@code
   *
   *       GeneratorConfig config = new GeneratorConfig();
   *       config.setClassesDirectory("./target/classes");
   *       config.setEntityBeanPackage("org.example.domain");
   *       ...
   *
   * }</pre>
   */
  public void setEntityBeanPackage(String entityBeanPackage) {
    this.entityBeanPackage = entityBeanPackage;
    if (destPackage == null) {
      setDestPackage(entityBeanPackage + ".query");
    }
  }

  /**
   * Return a list of specific entity classes to run generation on.
   */
  public List<File> getEntityClassFiles() {
    return entityClassFiles;
  }

  /**
   * Set specific entity classes to run generation on.
   */
  public void setEntityClassFiles(List<File> entityClassFiles) {
    this.entityClassFiles = entityClassFiles;
  }

  /**
   * Set a single specific entity class to run generation on.
   */
  public void setEntityClassFile(File entityClassFile) {
    this.entityClassFiles = Collections.singletonList(entityClassFile);
  }

  /**
   * Return the destination directory where the generated java source code will be written to.
   */
  public String getDestDirectory() {
    return destDirectory;
  }

  /**
   * Set the destination directory where the generated java source code will be written to.
   * <pre>{@code
   *
   *       GeneratorConfig config = new GeneratorConfig();
   *       config.setClassesDirectory("./target/classes");
   *       config.setEntityBeanPackage("org.example.domain");
   *
   *       config.setDestDirectory("./src/main/java");
   *       config.setDestPackage("org.example.domain.query");
   *       ...
   *
   * }</pre>
   */
  public void setDestDirectory(String destDirectory) {
    this.destDirectory = destDirectory;
  }

  /**
   * Return the destination directory for where the META-INF/ebean-typequery.mf manifest is written to.
   */
  public String getDestResourceDirectory() {
    return destResourceDirectory;
  }

  /**
   * Set the destination directory for where the META-INF/ebean-typequery.mf manifest is written to.
   */
  public void setDestResourceDirectory(String destResourceDirectory) {
    this.destResourceDirectory = destResourceDirectory;
  }

  /**
   * Return the package name used for the generated query beans.
   */
  public String getDestPackage() {
    return destPackage;
  }

  /**
   * Set the package name used to the generated query beans.
   * <p>
   * If not set this defaults to 'query' subpackage of the entity bean packages.
   * </p>
   * <p>
   * <p>
   * For example, this is typically a subpackage of the entity beans.
   * </p>
   * <pre>{@code
   *
   *       GeneratorConfig config = new GeneratorConfig();
   *       config.setClassesDirectory("./target/classes");
   *       config.setDestDirectory("./src/main/java");
   *
   *       config.setEntityBeanPackage("org.example.domain");
   *       config.setDestPackage("org.example.domain.query");
   *       ...
   *
   * }</pre>
   */
  public void setDestPackage(String destPackage) {
    this.destPackage = destPackage;
    this.destAssocPackage = destPackage + ".assoc";
  }

  private String deriveFinderPackage(String destPackage) {
    int lastDotPos = destPackage.lastIndexOf('.');
    if (lastDotPos == -1) {
      return destPackage + ".finder";
    }
    return destPackage.substring(0, lastDotPos) + ".finder";
  }

  /**
   * Return true if the where() method should be included on the generated finder.
   */
  public boolean isAddFinderWhereMethod() {
    return addFinderWhereMethod;
  }

  /**
   * Set true if the where() method should be included on the generated finder.
   * <p>
   * This defaults to true.
   * </p>
   */
  public void setAddFinderWhereMethod(boolean addFinderWhereMethod) {
    this.addFinderWhereMethod = addFinderWhereMethod;
  }

  /**
   * Return true if the text() method should be included on the generated finder.
   * <p>
   * This defaults to true.
   * </p>
   */
  public boolean isAddFinderTextMethod() {
    return addFinderTextMethod;
  }

  /**
   * Set if the text() method should be included on the generated finder.
   */
  public void setAddFinderTextMethod(boolean addFinderTextMethod) {
    this.addFinderTextMethod = addFinderTextMethod;
  }

  /**
   * Return true if extra generated methods on finder should be public.
   */
  public boolean isAddFinderWherePublic() {
    return addFinderWherePublic;
  }

  /**
   * Set to true if extra generated methods on finder should be public.
   * <p>
   * By default the methods are added as protected.
   * </p>
   */
  public void setAddFinderWherePublic(boolean addFinderWherePublic) {
    this.addFinderWherePublic = addFinderWherePublic;
  }

  /**
   * Return true if existing finders should be overwritten.
   */
  public boolean isOverwriteExistingFinders() {
    return overwriteExistingFinders;
  }

  /**
   * Set to true if existing finders should be overwritten.
   * <p>
   * This defaults to false.
   * </p>
   */
  public void setOverwriteExistingFinders(boolean overwriteExistingFinders) {
    this.overwriteExistingFinders = overwriteExistingFinders;
  }

  /**
   * Return the package name for the 'associated' query beans.
   * This is a 'assoc' subpackage from the query bean package.
   */
  public String getAssocPackage() {
    return destAssocPackage;
  }

  /**
   * Return the maximum path depth used in the generated code.
   */
  public int getMaxPathTraversalDepth() {
    return maxPathTraversalDepth;
  }

  /**
   * Set the maximum path depth used in the generated code.
   * <p>
   * This maximum value is included in the generated source code and puts a limit
   * on the depth of associated bean traversals you can use. You can manually
   * change the maximum depth on the root query bean as desired.
   * </p>
   */
  public void setMaxPathTraversalDepth(int maxPathTraversalDepth) {
    this.maxPathTraversalDepth = maxPathTraversalDepth;
  }

  /**
   * Return true if code generation orientated to AOP style (so minimal constructor).
   */
  public boolean isAopStyle() {
    return aopStyle;
  }

  /**
   * Set to false if code generation should use verbose manual style (not minimal AOP style).
   */
  public void setAopStyle(boolean aopStyle) {
    this.aopStyle = aopStyle;
  }

  public void appendLangSemiColon(Writer writer) throws IOException {
    if (isJava()) {
      writer.append(";");
    }
  }

  public LangAdapter lang() {
    return languageAdapter;
  }
}
