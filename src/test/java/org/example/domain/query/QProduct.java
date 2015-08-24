package org.example.domain.query;

import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TQRootBean;
import org.example.domain.Product;

public class QProduct extends TQRootBean<Product,QProduct> {

  public PLong<QProduct> id;
  public PLong<QProduct> version;
  public PTimestamp<QProduct> whenCreated;
  public PTimestamp<QProduct> whenUpdated;
  public PString<QProduct> sku;
  public PString<QProduct> name;

  public QProduct() {
    this(3);
  }
  public QProduct(int maxDepth) {
    super(Product.class);
    setRoot(this);
    this.id = new PLong<>("id", this);
    this.version = new PLong<>("version", this);
    this.whenCreated = new PTimestamp<>("whenCreated", this);
    this.whenUpdated = new PTimestamp<>("whenUpdated", this);
    this.sku = new PString<>("sku", this);
    this.name = new PString<>("name", this);
  }
}
