package org.avaje.ebean.typequery.generator.write;

import java.io.IOException;
import java.io.Writer;

/**
 *
 */
public class PropertyMeta {

  final String name;

  final PropertyType type;

  public PropertyMeta(String name, PropertyType type) {
    this.name = name;
    this.type = type;
  }

  public boolean isAssociation() {
    return type.isAssociation();
  }


  public String getTypeDefn(String shortName, boolean assoc) {
    return type.getTypeDefn(shortName, assoc);
  }

  public void writeFieldDefn(Writer writer, String shortName, boolean assoc) throws IOException {

    writer.append("  public ");
    writer.append(getTypeDefn(shortName, assoc));
    writer.append(" ").append(name).append(";");
  }

  public void writeConstructorSimple(Writer writer, String shortName, boolean assoc) throws IOException {

    if (!type.isAssociation()) {
      writer.append("    this.").append(name).append(" = new ");
      type.writeConstructor(writer, name, assoc);
    }
  }

  public void writeConstructorAssoc(Writer writer, String shortName, boolean assoc) throws IOException {
    if (type.isAssociation()) {
      if (assoc) {
        writer.append("  ");
      }
      writer.append("    this.").append(name).append(" = new ");
      type.writeConstructor(writer, name, assoc);
    }
  }

}
