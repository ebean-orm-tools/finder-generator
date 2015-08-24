package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PEnum;
import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PSqlDate;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TQPath;
import org.example.domain.Order.Status;

public class QAssocOrder<R> {

  public PLong<R> id;
  public PLong<R> version;
  public PTimestamp<R> whenCreated;
  public PTimestamp<R> whenUpdated;
  public PEnum<R,Status> status;
  public PSqlDate<R> orderDate;
  public PSqlDate<R> shipDate;
  public QAssocCustomer<R> customer;
  public QAssocAddress<R> shippingAddress;
  public QAssocOrderDetail<R> details;

  public QAssocOrder(String name, R root, int depth) {
    this(name, root, null, depth);
  }
  public QAssocOrder(String name, R root, String prefix, int depth) {
    String path = TQPath.add(prefix, name);
    this.id = new PLong<>("id", root, path);
    this.version = new PLong<>("version", root, path);
    this.whenCreated = new PTimestamp<>("whenCreated", root, path);
    this.whenUpdated = new PTimestamp<>("whenUpdated", root, path);
    this.status = new PEnum<>("status", root, path);
    this.orderDate = new PSqlDate<>("orderDate", root, path);
    this.shipDate = new PSqlDate<>("shipDate", root, path);
    if (--depth > 0) {
      this.customer = new QAssocCustomer<>("customer", root, path, depth);
      this.shippingAddress = new QAssocAddress<>("shippingAddress", root, path, depth);
      this.details = new QAssocOrderDetail<>("details", root, path, depth);
    }
  }
}
