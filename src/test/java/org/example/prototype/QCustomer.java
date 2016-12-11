package org.example.prototype;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.typequery.PLong;
import io.ebean.typequery.PString;
import io.ebean.typequery.PUtilDate;
import io.ebean.typequery.TQRootBean;
import org.example.domain.Customer;
import org.example.prototype.assoc.QAssocContact;


public class QCustomer extends TQRootBean<Customer,QCustomer> {

  public PLong<QCustomer> id;

  public PString<QCustomer> name;

  public PUtilDate<QCustomer> registered;

  public QAssocContact<QCustomer> contacts;

  public QCustomer() {
    this(Ebean.getDefaultServer());
  }

  public QCustomer(EbeanServer server) {
    super(Customer.class, server);
    setRoot(this);
    this.id = new PLong<>("id", this);
    this.name = new PString<>("name", this);
    this.registered = new PUtilDate<>("registered", this);
    this.contacts = new QAssocContact<>("contacts", this, 5);
  }

}
