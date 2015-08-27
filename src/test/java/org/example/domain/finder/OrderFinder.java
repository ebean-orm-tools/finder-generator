package org.example.domain.finder;

import com.avaje.ebean.Finder;
import org.example.domain.Order;

public class OrderFinder extends Finder<Long,Order> {

  /**
   * Construct using the default EbeanServer.
   */
  public OrderFinder() {
    super(Order.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public OrderFinder(String serverName) {
    super(serverName, Order.class);
  }
}
