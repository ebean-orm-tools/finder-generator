package org.example.domain.query;

import org.avaje.ebean.typequery.PEnum;
import org.avaje.ebean.typequery.PSqlDate;
import org.avaje.ebean.typequery.TQRootBean;
import org.example.domain.Order;
import org.example.domain.Order.Status;
import org.example.domain.query.assoc.QAssocAddress;
import org.example.domain.query.assoc.QAssocCustomer;
import org.example.domain.query.assoc.QAssocOrderDetail;

public class QOrder extends TQRootBean<Order,QOrder> {

  public PEnum<QOrder,Status> status;
  public PSqlDate<QOrder> orderDate;
  public PSqlDate<QOrder> shipDate;
  public QAssocCustomer<QOrder> customer;
  public QAssocAddress<QOrder> shippingAddress;
  public QAssocOrderDetail<QOrder> details;

  public QOrder() {
    super(Order.class);
    setRoot(this);
    this.status = new PEnum<>("status", this);
    this.orderDate = new PSqlDate<>("orderDate", this);
    this.shipDate = new PSqlDate<>("shipDate", this);
    this.customer = new QAssocCustomer<>("customer", this, 5);
    this.shippingAddress = new QAssocAddress<>("shippingAddress", this, 5);
    this.details = new QAssocOrderDetail<>("details", this, 5);
  }
}
