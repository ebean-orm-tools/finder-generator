package org.avaje.ebean.typequery.generator;

/**
 * Configuration for the code generation.
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
public class GeneratorConfig {

  /**
   * The directory where the compiled classes are found.
   */
  String classesDirectory;

  String entityBeanPackage;

  String destDirectory;

  String destPackage;

  String destAssocPackage;

  /**
   * The maximum path depth allowed in the generated code.
   */
  int maxPathTraversalDepth = 4;

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
   *
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
   *
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
   * Return the package name used for the generated query beans.
   */
  public String getDestPackage() {
    return destPackage;
  }

  /**
   * Set the package name used to the generated query beans.
   * For example, this is typically a subpackage of the entity beans.
   *
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
  public void setDestPackage(String destPackage) {
    this.destPackage = destPackage;
    this.destAssocPackage = destPackage+".assoc";
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
}
