package org.avaje.ebean.typequery.generator;

import org.junit.Test;

public class GeneratorTest {

  @Test
  public void test() {


    GeneratorConfig config = new GeneratorConfig();
    config.setSourceDirectory("/home/rob/github/avaje-ebeanorm-typequery/target/test-classes");
    config.setSourcePackage("org.example.domain");
    config.setDestDirectory("./");
    config.setDestPackage("org.example.domain.query");

    Generator generator = new Generator(config);
    generator.process();

  }
}