package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PBoolean;
import org.avaje.ebean.typequery.PEnum;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PUtilDate;
import org.avaje.ebean.typequery.TQPath;
import org.example.domain.Customer.Status;
import org.example.domain.query.assoc.QAssocAddress;
import org.example.domain.query.assoc.QAssocContact;

public class QAssocCustomer<R> {

  public PEnum<R,Status> status;
  public PBoolean<R> inactive;
  public PString<R> name;
  public PUtilDate<R> registered;
  public PString<R> comments;
  public QAssocAddress<R> billingAddress;
  public QAssocAddress<R> shippingAddress;
  public QAssocContact<R> contacts;

  public QAssocCustomer(String name, R root, int depth) {
    this(name, root, null, depth);
  }
  public QAssocCustomer(String name, R root, String prefix, int depth) {
    String path = TQPath.add(prefix, name);
    this.status = new PEnum<>("status", root, path);
    this.inactive = new PBoolean<>("inactive", root, path);
    this.name = new PString<>("name", root, path);
    this.registered = new PUtilDate<>("registered", root, path);
    this.comments = new PString<>("comments", root, path);
    if (--depth > 0) {
      this.billingAddress = new QAssocAddress<>("billingAddress", root, path, depth);
      this.shippingAddress = new QAssocAddress<>("shippingAddress", root, path, depth);
      this.contacts = new QAssocContact<>("contacts", root, path, depth);
    }
  }
}
