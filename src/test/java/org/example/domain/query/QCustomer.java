package org.example.domain.query;

import org.avaje.ebean.typequery.PBoolean;
import org.avaje.ebean.typequery.PEnum;
import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.PUtilDate;
import org.avaje.ebean.typequery.TQRootBean;
import org.avaje.ebean.typequery.TypeQueryBean;
import org.example.domain.Customer;
import org.example.domain.Customer.Status;
import org.example.domain.query.assoc.QAssocAddress;
import org.example.domain.query.assoc.QAssocContact;

@TypeQueryBean
public class QCustomer extends TQRootBean<Customer,QCustomer> {

  public PLong<QCustomer> id;
  public PLong<QCustomer> version;
  public PTimestamp<QCustomer> whenCreated;
  public PTimestamp<QCustomer> whenUpdated;
  public PEnum<QCustomer,Status> status;
  public PBoolean<QCustomer> inactive;
  public PString<QCustomer> name;
  public PUtilDate<QCustomer> registered;
  public PString<QCustomer> comments;
  public QAssocAddress<QCustomer> billingAddress;
  public QAssocAddress<QCustomer> shippingAddress;
  public QAssocContact<QCustomer> contacts;

  public QCustomer() {
    this(3);
  }
  public QCustomer(int maxDepth) {
    super(Customer.class);
    setRoot(this);
    this.id = new PLong<>("id", this);
    this.version = new PLong<>("version", this);
    this.whenCreated = new PTimestamp<>("whenCreated", this);
    this.whenUpdated = new PTimestamp<>("whenUpdated", this);
    this.status = new PEnum<>("status", this);
    this.inactive = new PBoolean<>("inactive", this);
    this.name = new PString<>("name", this);
    this.registered = new PUtilDate<>("registered", this);
    this.comments = new PString<>("comments", this);
    this.billingAddress = new QAssocAddress<>("billingAddress", this, maxDepth);
    this.shippingAddress = new QAssocAddress<>("shippingAddress", this, maxDepth);
    this.contacts = new QAssocContact<>("contacts", this, maxDepth);
  }
}
