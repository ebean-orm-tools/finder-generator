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
   * Default to maven target/classes.
   */
  String classesDirectory = "target/classes";

  String entityBeanPackage;

  String destDirectory = "src/main/java";

  String destResourceDirectory = "src/main/resources";

  String destPackage;

  String destAssocPackage;

  String destFinderPackage;

  boolean aopStyle = true;

  /**
   * The maximum path depth allowed in the generated code (non AOP manual style).
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
    if (destPackage == null) {
      setDestPackage(entityBeanPackage+".query");
    }
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
   *
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
    this.destAssocPackage = destPackage+".assoc";
    if (destFinderPackage == null) {
      destFinderPackage = deriveFinderPackage(destPackage);
    }
  }

  private String deriveFinderPackage(String destPackage) {
    int lastDotPos = destPackage.lastIndexOf('.');
    if (lastDotPos == -1) {
      return destPackage + ".finder";
    }
    return destPackage.substring(0, lastDotPos) + ".finder";
  }

  /**
   * Return the package that the 'finders' if generated go into.
   */
  public String getDestFinderPackage() {
    return destFinderPackage;
  }

  /**
   * Set the package that the 'finders' if generated go into.
   * <p>
   * If not set this defaults to a 'finder' subpackage.
   * </p>
   */
  public void setDestFinderPackage(String destFinderPackage) {
    this.destFinderPackage = destFinderPackage;
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
}
