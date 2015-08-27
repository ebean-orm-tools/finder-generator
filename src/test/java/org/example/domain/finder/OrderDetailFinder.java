package org.example.domain.finder;

import com.avaje.ebean.Finder;
import org.example.domain.OrderDetail;

public class OrderDetailFinder extends Finder<Long,OrderDetail> {

  /**
   * Construct using the default EbeanServer.
   */
  public OrderDetailFinder() {
    super(OrderDetail.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public OrderDetailFinder(String serverName) {
    super(serverName, OrderDetail.class);
  }
}
