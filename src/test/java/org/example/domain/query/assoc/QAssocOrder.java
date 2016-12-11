package org.example.domain.query.assoc;

import io.ebean.typequery.PEnum;
import io.ebean.typequery.PLong;
import io.ebean.typequery.PSqlDate;
import io.ebean.typequery.PTimestamp;
import io.ebean.typequery.TQAssocBean;
import io.ebean.typequery.TQProperty;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.Order;
import org.example.domain.Order.Status;
import org.example.domain.query.QOrder;

/**
 * Association query bean for AssocOrder.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QAssocOrder<R> extends TQAssocBean<Order,R> {

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

  /**
   * Eagerly fetch this association loading the specified properties.
   */
  @SafeVarargs
  public final R fetch(TQProperty<QOrder>... properties) {
    return fetchProperties(properties);
  }

  public QAssocOrder(String name, R root) {
    super(name, root);
  }
}
