package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TQPath;

public class QAssocProduct<R> {

  public PLong<R> id;
  public PLong<R> version;
  public PTimestamp<R> whenCreated;
  public PTimestamp<R> whenUpdated;
  public PString<R> sku;
  public PString<R> name;

  public QAssocProduct(String name, R root, int depth) {
    this(name, root, null, depth);
  }
  public QAssocProduct(String name, R root, String prefix, int depth) {
    String path = TQPath.add(prefix, name);
    this.id = new PLong<>("id", root, path);
    this.version = new PLong<>("version", root, path);
    this.whenCreated = new PTimestamp<>("whenCreated", root, path);
    this.whenUpdated = new PTimestamp<>("whenUpdated", root, path);
    this.sku = new PString<>("sku", root, path);
    this.name = new PString<>("name", root, path);
  }
}
