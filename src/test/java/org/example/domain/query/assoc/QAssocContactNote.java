package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TQPath;

public class QAssocContactNote<R> {

  public PLong<R> id;
  public PLong<R> version;
  public PTimestamp<R> whenCreated;
  public PTimestamp<R> whenUpdated;
  public QAssocContact<R> contact;
  public PString<R> title;
  public PString<R> note;

  public QAssocContactNote(String name, R root, int depth) {
    this(name, root, null, depth);
  }
  public QAssocContactNote(String name, R root, String prefix, int depth) {
    String path = TQPath.add(prefix, name);
    this.id = new PLong<>("id", root, path);
    this.version = new PLong<>("version", root, path);
    this.whenCreated = new PTimestamp<>("whenCreated", root, path);
    this.whenUpdated = new PTimestamp<>("whenUpdated", root, path);
    this.title = new PString<>("title", root, path);
    this.note = new PString<>("note", root, path);
    if (--depth > 0) {
      this.contact = new QAssocContact<>("contact", root, path, depth);
    }
  }
}
