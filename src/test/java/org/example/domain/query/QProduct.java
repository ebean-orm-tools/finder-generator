package org.example.domain.query;

import io.ebean.EbeanServer;
import io.ebean.typequery.PJodaDateTime;
import io.ebean.typequery.PLong;
import io.ebean.typequery.PMonth;
import io.ebean.typequery.PString;
import io.ebean.typequery.PTimestamp;
import io.ebean.typequery.TQRootBean;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.Product;

/**
 * Query bean for Product.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QProduct extends TQRootBean<Product,QProduct> {

  private static final QProduct _alias = new QProduct(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QProduct alias() {
    return _alias;
  }

  public PLong<QProduct> id;
  public PLong<QProduct> version;
  public PTimestamp<QProduct> whenCreated;
  public PTimestamp<QProduct> whenUpdated;
  public PString<QProduct> sku;
  public PString<QProduct> name;
  public PJodaDateTime<QProduct> jdDateTime;
  public PMonth<QProduct> month;


  /**
   * Construct with a given EbeanServer.
   */
  public QProduct(EbeanServer server) {
    super(Product.class, server);
  }

  /**
   * Construct using the default EbeanServer.
   */
  public QProduct() {
    super(Product.class);
  }

  /**
   * Construct for Alias.
   */
  private QProduct(boolean dummy) {
    super(dummy);
  }
}
