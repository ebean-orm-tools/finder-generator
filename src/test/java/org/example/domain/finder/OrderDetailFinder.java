package org.example.domain.finder;

import io.ebean.Finder;
import org.example.domain.OrderDetail;
import org.example.domain.query.QOrderDetail;

public class OrderDetailFinder extends Finder<Long,OrderDetail> {

  /**
   * Construct using the default Database.
   */
  public OrderDetailFinder() {
    super(OrderDetail.class);
  }


  /**
   * Start a new typed query.
   */
  protected QOrderDetail where() {
     return new QOrderDetail(db());
  }

  /**
   * Start a new document store query.
   */
  protected QOrderDetail text() {
     return new QOrderDetail(db()).text();
  }
}
