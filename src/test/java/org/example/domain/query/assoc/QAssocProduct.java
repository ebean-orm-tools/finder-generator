package org.example.domain.query.assoc;

import io.ebean.typequery.PJodaDateTime;
import io.ebean.typequery.PLong;
import io.ebean.typequery.PMonth;
import io.ebean.typequery.PString;
import io.ebean.typequery.PTimestamp;
import io.ebean.typequery.TQAssocBean;
import io.ebean.typequery.TQProperty;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.Product;
import org.example.domain.query.QProduct;

/**
 * Association query bean for AssocProduct.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QAssocProduct<R> extends TQAssocBean<Product,R> {

  public PLong<R> id;
  public PLong<R> version;
  public PTimestamp<R> whenCreated;
  public PTimestamp<R> whenUpdated;
  public PString<R> sku;
  public PString<R> name;
  public PJodaDateTime<R> jdDateTime;
  public PMonth<R> month;

  /**
   * Eagerly fetch this association loading the specified properties.
   */
  @SafeVarargs
  public final R fetch(TQProperty<QProduct>... properties) {
    return fetchProperties(properties);
  }

  public QAssocProduct(String name, R root) {
    super(name, root);
  }
}
