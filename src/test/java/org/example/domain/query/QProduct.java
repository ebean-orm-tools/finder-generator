package org.example.domain.query;

import com.avaje.ebean.EbeanServer;
import org.avaje.ebean.typequery.PJodaDateTime;
import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PMonth;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TQRootBean;
import org.avaje.ebean.typequery.TypeQueryBean;
import org.example.domain.Product;

@TypeQueryBean
public class QProduct extends TQRootBean<Product,QProduct> {

  public PLong<QProduct> id;
  public PLong<QProduct> version;
  public PTimestamp<QProduct> whenCreated;
  public PTimestamp<QProduct> whenUpdated;
  public PString<QProduct> sku;
  public PString<QProduct> name;
  public PJodaDateTime<QProduct> jdDateTime;
  public PMonth<QProduct> month;

  /**
   * Construct using the default EbeanServer.
   */
  public QProduct() {
    super(Product.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public QProduct(EbeanServer server) {
    super(Product.class, server);
  }
}
