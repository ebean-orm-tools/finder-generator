package org.example.domain.query;

import io.ebean.EbeanServer;
import io.ebean.typequery.PBoolean;
import io.ebean.typequery.PEnum;
import io.ebean.typequery.PLong;
import io.ebean.typequery.PString;
import io.ebean.typequery.PTimestamp;
import io.ebean.typequery.PUtilDate;
import io.ebean.typequery.TQRootBean;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.Customer;
import org.example.domain.Customer.Status;
import org.example.domain.query.assoc.QAssocAddress;
import org.example.domain.query.assoc.QAssocContact;

/**
 * Query bean for Customer.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QCustomer extends TQRootBean<Customer,QCustomer> {

  private static final QCustomer _alias = new QCustomer(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QCustomer alias() {
    return _alias;
  }

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


  /**
   * Construct with a given EbeanServer.
   */
  public QCustomer(EbeanServer server) {
    super(Customer.class, server);
  }

  /**
   * Construct using the default EbeanServer.
   */
  public QCustomer() {
    super(Customer.class);
  }

  /**
   * Construct for Alias.
   */
  private QCustomer(boolean dummy) {
    super(dummy);
  }
}
