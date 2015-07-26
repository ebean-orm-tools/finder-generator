package org.avaje.ebean.typequery.generator.write;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 *
 */
public class PropertyTypeAssoc extends PropertyType {

  private final String assocPackage;

  //  public QAssocContact<QCustomer> contacts;

  public PropertyTypeAssoc(String qAssocName, String assocPackage) {
    super(qAssocName);
    this.assocPackage = assocPackage;
  }

  @Override
  public boolean isAssociation() {
    return true;
  }

  @Override
  public void addImports(Set<String> allImports) {
    allImports.add(assocPackage+"."+propertyType);
  }

  @Override
  public void writeConstructor(Writer writer, String name, boolean assoc) throws IOException {

    writer.append(propertyType).append("<>(\"").append(name).append("\"");
    if (assoc) {
      //this.notes = new QAssocContactNote<>("notes", root, path, depth);
      writer.append(", root, path, depth);").append(NEWLINE);

    } else {
      writer.append(", this, 5);").append(NEWLINE);
    }
  }
}
