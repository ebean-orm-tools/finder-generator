package org.example.domain.query;

import org.avaje.ebean.typequery.PDouble;
import org.avaje.ebean.typequery.PInteger;
import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TQRootBean;
import org.example.domain.OrderDetail;
import org.example.domain.query.assoc.QAssocOrder;
import org.example.domain.query.assoc.QAssocProduct;

public class QOrderDetail extends TQRootBean<OrderDetail,QOrderDetail> {

  public PLong<QOrderDetail> id;
  public PLong<QOrderDetail> version;
  public PTimestamp<QOrderDetail> whenCreated;
  public PTimestamp<QOrderDetail> whenUpdated;
  public QAssocOrder<QOrderDetail> order;
  public PInteger<QOrderDetail> orderQty;
  public PInteger<QOrderDetail> shipQty;
  public PDouble<QOrderDetail> unitPrice;
  public QAssocProduct<QOrderDetail> product;

  public QOrderDetail() {
    this(3);
  }
  public QOrderDetail(int maxDepth) {
    super(OrderDetail.class);
    setRoot(this);
    this.id = new PLong<>("id", this);
    this.version = new PLong<>("version", this);
    this.whenCreated = new PTimestamp<>("whenCreated", this);
    this.whenUpdated = new PTimestamp<>("whenUpdated", this);
    this.orderQty = new PInteger<>("orderQty", this);
    this.shipQty = new PInteger<>("shipQty", this);
    this.unitPrice = new PDouble<>("unitPrice", this);
    this.order = new QAssocOrder<>("order", this, maxDepth);
    this.product = new QAssocProduct<>("product", this, maxDepth);
  }
}
