package org.avaje.ebean.typequery.generator.write;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 */
public class PropertyType {

  public static final String NEWLINE = SimpleEntityBeanWriter.NEWLINE;


  final String propertyType;

  public PropertyType(String propertyType) {
    this.propertyType = propertyType;
  }

  public boolean isAssociation() {
    return false;
  }

  public String getTypeDefn(String shortName, boolean assoc) {
    if (assoc) {
      //    PLong<R>
      return propertyType+"<R>";

    } else {
      //    PLong<QCustomer>
      return propertyType+"<Q"+shortName+">";
    }
  }

  public void addImports(Set<String> allImports) {

    allImports.add("org.avaje.ebean.typequery."+propertyType);
  }

  public void writeConstructor(Writer writer, String name, boolean assoc) throws IOException {

    //PLong<>("id", this);
    //PLong<>("id", root, path);

    writer.append(propertyType).append("<>(\"").append(name).append("\"");
    if (assoc) {
      writer.append(", root, path);").append(NEWLINE);

    } else {
      writer.append(", this);").append(NEWLINE);
    }
  }
}
