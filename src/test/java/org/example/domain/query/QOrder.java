package org.example.domain.query;

import io.ebean.Database;
import io.ebean.typequery.PEnum;
import io.ebean.typequery.PLong;
import io.ebean.typequery.PSqlDate;
import io.ebean.typequery.PTimestamp;
import io.ebean.typequery.TQRootBean;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.Order;
import org.example.domain.Order.Status;
import org.example.domain.query.assoc.QAssocAddress;
import org.example.domain.query.assoc.QAssocCustomer;
import org.example.domain.query.assoc.QAssocOrderDetail;

/**
 * Query bean for Order.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QOrder extends TQRootBean<Order,QOrder> {

  private static final QOrder _alias = new QOrder(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QOrder alias() {
    return _alias;
  }

  public PLong<QOrder> id;
  public PLong<QOrder> version;
  public PTimestamp<QOrder> whenCreated;
  public PTimestamp<QOrder> whenUpdated;
  public PEnum<QOrder,Status> status;
  public PSqlDate<QOrder> orderDate;
  public PSqlDate<QOrder> shipDate;
  public QAssocCustomer<QOrder> customer;
  public QAssocAddress<QOrder> shippingAddress;
  public QAssocOrderDetail<QOrder> details;


  /**
   * Construct with a given Database.
   */
  public QOrder(Database database) {
    super(Order.class, database);
  }

  /**
   * Construct using the default Database.
   */
  public QOrder() {
    super(Order.class);
  }

  /**
   * Construct for Alias.
   */
  private QOrder(boolean dummy) {
    super(dummy);
  }
}
