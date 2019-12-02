package org.example.domain.finder;

import io.ebean.Finder;
import org.example.domain.Order;

public class OrderFinder extends Finder<Long,Order> {

  /**
   * Construct using the default Database.
   */
  public OrderFinder() {
    super(Order.class);
  }

}
