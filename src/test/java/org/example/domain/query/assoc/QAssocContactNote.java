package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQPath;
import org.example.domain.query.assoc.QAssocContact;

public class QAssocContactNote<R> {

  public QAssocContact<R> contact;
  public PString<R> title;
  public PString<R> note;

  public QAssocContactNote(String name, R root, int depth) {
    this(name, root, null, depth);
  }
  public QAssocContactNote(String name, R root, String prefix, int depth) {
    String path = TQPath.add(prefix, name);
    this.title = new PString<>("title", root, path);
    this.note = new PString<>("note", root, path);
    if (--depth > 0) {
      this.contact = new QAssocContact<>("contact", root, path, depth);
    }
  }
}
