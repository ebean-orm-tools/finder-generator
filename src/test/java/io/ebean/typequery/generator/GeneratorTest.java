package io.ebean.typequery.generator;

import org.junit.Test;

import java.io.IOException;

public class GeneratorTest {

  @Test
  public void test() throws IOException {

    GeneratorConfig config = new GeneratorConfig();
    config.setClassesDirectory("./target/test-classes");
    config.setDestDirectory("./src/test/java");
    config.setDestResourceDirectory("./src/test/resources");

    config.setEntityBeanPackage("org.example.domain");
    //config.setDestPackage("org.example.domain.query");
    //config.setMaxPathTraversalDepth(3);
    //config.setAopStyle(false);

    config.setOverwriteExistingFinders(true);
    //config.setLang("kt");

    Generator generator = new Generator(config);
    generator.generateQueryBeans();
    generator.generateFinders();
    //generator.modifyEntityBeansAddFinderField();

  }
}