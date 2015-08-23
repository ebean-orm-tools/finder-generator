package org.example.domain.query;

import org.avaje.ebean.typequery.PDouble;
import org.avaje.ebean.typequery.PInteger;
import org.avaje.ebean.typequery.TQRootBean;
import org.example.domain.OrderDetail;
import org.example.domain.query.assoc.QAssocOrder;
import org.example.domain.query.assoc.QAssocProduct;

public class QOrderDetail extends TQRootBean<OrderDetail,QOrderDetail> {

  public QAssocOrder<QOrderDetail> order;
  public PInteger<QOrderDetail> orderQty;
  public PInteger<QOrderDetail> shipQty;
  public PDouble<QOrderDetail> unitPrice;
  public QAssocProduct<QOrderDetail> product;

  public QOrderDetail() {
    super(OrderDetail.class);
    setRoot(this);
    this.orderQty = new PInteger<>("orderQty", this);
    this.shipQty = new PInteger<>("shipQty", this);
    this.unitPrice = new PDouble<>("unitPrice", this);
    this.order = new QAssocOrder<>("order", this, 3);
    this.product = new QAssocProduct<>("product", this, 3);
  }
}
