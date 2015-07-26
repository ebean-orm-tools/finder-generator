package org.avaje.ebean.typequery.generator;

import org.junit.Test;

import java.io.IOException;

public class GeneratorTest {

  @Test
  public void test() throws IOException {

    GeneratorConfig config = new GeneratorConfig();
    config.setSourceDirectory("./target/test-classes");
    config.setSourcePackage("org.example.domain");
    config.setDestDirectory("./src/test/java");
    config.setDestPackage("org.example.domain.query");

    Generator generator = new Generator(config);
    generator.process();

  }
}