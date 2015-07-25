package org.avaje.ebean.typequery.generator;

/**
 * Created by rob on 25/07/15.
 */
public class GeneratorConfig {

  String sourceDirectory;

  String sourcePackage;

  String destDirectory;

  String destPackage;

  public String getSourceDirectory() {
    return sourceDirectory;
  }

  public void setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  public String getSourcePackage() {
    return sourcePackage;
  }

  public void setSourcePackage(String sourcePackage) {
    this.sourcePackage = sourcePackage;
  }

  public String getDestDirectory() {
    return destDirectory;
  }

  public void setDestDirectory(String destDirectory) {
    this.destDirectory = destDirectory;
  }

  public String getDestPackage() {
    return destPackage;
  }

  public void setDestPackage(String destPackage) {
    this.destPackage = destPackage;
  }
}
