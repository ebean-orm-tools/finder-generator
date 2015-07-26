package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PEnum;
import org.avaje.ebean.typequery.PSqlDate;
import org.avaje.ebean.typequery.TQPath;
import org.example.domain.Order.Status;
import org.example.domain.query.assoc.QAssocAddress;
import org.example.domain.query.assoc.QAssocCustomer;
import org.example.domain.query.assoc.QAssocOrderDetail;

public class QAssocOrder<R> {

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
