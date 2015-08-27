package org.example.domain.finder;

import com.avaje.ebean.Finder;
import org.example.domain.Product;

public class ProductFinder extends Finder<Long,Product> {

  /**
   * Construct using the default EbeanServer.
   */
  public ProductFinder() {
    super(Product.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public ProductFinder(String serverName) {
    super(serverName, Product.class);
  }
}
