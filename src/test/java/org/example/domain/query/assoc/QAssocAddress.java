package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TQPath;
import org.avaje.ebean.typequery.TypeQueryBean;

@TypeQueryBean
public class QAssocAddress<R> {

  public PLong<R> id;
  public PLong<R> version;
  public PTimestamp<R> whenCreated;
  public PTimestamp<R> whenUpdated;
  public PString<R> line1;
  public PString<R> line2;
  public PString<R> city;
  public QAssocCountry<R> country;

  public QAssocAddress(String name, R root, int depth) {
    this(name, root, null, depth);
  }
  public QAssocAddress(String name, R root, String prefix, int depth) {
    String path = TQPath.add(prefix, name);
    this.id = new PLong<>("id", root, path);
    this.version = new PLong<>("version", root, path);
    this.whenCreated = new PTimestamp<>("whenCreated", root, path);
    this.whenUpdated = new PTimestamp<>("whenUpdated", root, path);
    this.line1 = new PString<>("line1", root, path);
    this.line2 = new PString<>("line2", root, path);
    this.city = new PString<>("city", root, path);
    if (--depth > 0) {
      this.country = new QAssocCountry<>("country", root, path, depth);
    }
  }
}
