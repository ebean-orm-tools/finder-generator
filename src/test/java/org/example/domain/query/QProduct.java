package org.example.domain.query;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQRootBean;
import org.example.domain.Product;

public class QProduct extends TQRootBean<Product,QProduct> {

  public PString<QProduct> sku;
  public PString<QProduct> name;

  public QProduct() {
    this(3);
  }
  public QProduct(int maxDepth) {
    super(Product.class);
    setRoot(this);
    this.sku = new PString<>("sku", this);
    this.name = new PString<>("name", this);
  }
}
