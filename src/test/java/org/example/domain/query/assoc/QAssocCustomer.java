package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PBoolean;
import org.avaje.ebean.typequery.PEnum;
import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.PUtilDate;
import org.avaje.ebean.typequery.TQAssocBean;
import org.avaje.ebean.typequery.TQProperty;
import org.avaje.ebean.typequery.TypeQueryBean;
import org.example.domain.Customer;
import org.example.domain.Customer.Status;

/**
 * Association query bean for AssocCustomer.
 */
@TypeQueryBean
public class QAssocCustomer<R> extends TQAssocBean<Customer,R> {

  public PLong<R> id;
  public PLong<R> version;
  public PTimestamp<R> whenCreated;
  public PTimestamp<R> whenUpdated;
  public PEnum<R,Status> status;
  public PBoolean<R> inactive;
  public PString<R> name;
  public PUtilDate<R> registered;
  public PString<R> comments;
  public QAssocAddress<R> billingAddress;
  public QAssocAddress<R> shippingAddress;
  public QAssocContact<R> contacts;

  /**
   * Eagerly fetch this association loading the specified properties.
   */
  @SafeVarargs
  public final R fetch(TQProperty<QAssocCustomer>... properties) {
    return fetchProperties(properties);
  }

  public QAssocCustomer(String name, R root) {
    super(name, root);
  }
}
