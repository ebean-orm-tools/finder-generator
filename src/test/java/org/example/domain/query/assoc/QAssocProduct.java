package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQPath;

public class QAssocProduct<R> {

  public PString<R> sku;
  public PString<R> name;

  public QAssocProduct(String name, R root, int depth) {
    this(name, root, null, depth);
  }
  public QAssocProduct(String name, R root, String prefix, int depth) {
    String path = TQPath.add(prefix, name);
    this.sku = new PString<>("sku", root, path);
    this.name = new PString<>("name", root, path);
  }
}
