package org.avaje.ebean.typequery.generator.write;

import org.avaje.ebean.typequery.generator.GeneratorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes the <code>ebean-typequery.mf</code> manifest file.
 */
public class SimpleManifestWriter {

  protected static final Logger logger = LoggerFactory.getLogger(SimpleManifestWriter.class);

  public static final String NEWLINE = "\n";

  public static final String EBEAN_TYPEQUERY_MF = "ebean-typequery.mf";

  public static final String META_INF = "META-INF";

  protected final GeneratorConfig config;

  public SimpleManifestWriter(GeneratorConfig config) {
    this.config = config;
  }

  /**
   * Write the manifest file.
   */
  public void write() throws IOException {

    FileWriter writer = createFileWriter();
    writer.append("packages: ");
    writer.append(config.getDestPackage());
    writer.append(NEWLINE).append(NEWLINE);
    writer.flush();
    writer.close();
  }

  protected FileWriter createFileWriter() throws IOException {

    String destDirectory = config.getDestResourceDirectory();
    File destDir = new File(destDirectory);

    //String packageAsDir = asSlashNotation(destPackage);

    File packageDir = new File(destDir, META_INF);
    if (!packageDir.exists() && !packageDir.mkdirs()) {
      logger.error("Failed to create directory [{}] for ebean-typequery.mf", packageDir.getAbsoluteFile());
    }

    File dest = new File(packageDir, EBEAN_TYPEQUERY_MF);
    logger.info("writing {}", dest.getAbsolutePath());

    return new FileWriter(dest, false);
  }

}
