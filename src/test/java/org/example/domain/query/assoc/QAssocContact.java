package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TQPath;

public class QAssocContact<R> {

  public PLong<R> id;
  public PLong<R> version;
  public PTimestamp<R> whenCreated;
  public PTimestamp<R> whenUpdated;
  public PString<R> firstName;
  public PString<R> lastName;
  public PString<R> email;
  public PString<R> phone;
  public QAssocCustomer<R> customer;
  public QAssocContactNote<R> notes;

  public QAssocContact(String name, R root, int depth) {
    this(name, root, null, depth);
  }
  public QAssocContact(String name, R root, String prefix, int depth) {
    String path = TQPath.add(prefix, name);
    this.id = new PLong<>("id", root, path);
    this.version = new PLong<>("version", root, path);
    this.whenCreated = new PTimestamp<>("whenCreated", root, path);
    this.whenUpdated = new PTimestamp<>("whenUpdated", root, path);
    this.firstName = new PString<>("firstName", root, path);
    this.lastName = new PString<>("lastName", root, path);
    this.email = new PString<>("email", root, path);
    this.phone = new PString<>("phone", root, path);
    if (--depth > 0) {
      this.customer = new QAssocCustomer<>("customer", root, path, depth);
      this.notes = new QAssocContactNote<>("notes", root, path, depth);
    }
  }
}