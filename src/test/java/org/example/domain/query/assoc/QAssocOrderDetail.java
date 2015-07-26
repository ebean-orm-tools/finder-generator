package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PDouble;
import org.avaje.ebean.typequery.PInteger;
import org.avaje.ebean.typequery.TQPath;
import org.example.domain.query.assoc.QAssocOrder;
import org.example.domain.query.assoc.QAssocProduct;

public class QAssocOrderDetail<R> {

  public QAssocOrder<R> order;
  public PInteger<R> orderQty;
  public PInteger<R> shipQty;
  public PDouble<R> unitPrice;
  public QAssocProduct<R> product;

  public QAssocOrderDetail(String name, R root, int depth) {
    this(name, root, null, depth);
  }
  public QAssocOrderDetail(String name, R root, String prefix, int depth) {
    String path = TQPath.add(prefix, name);
    this.orderQty = new PInteger<>("orderQty", root, path);
    this.shipQty = new PInteger<>("shipQty", root, path);
    this.unitPrice = new PDouble<>("unitPrice", root, path);
    if (--depth > 0) {
      this.order = new QAssocOrder<>("order", root, path, depth);
      this.product = new QAssocProduct<>("product", root, path, depth);
    }
  }
}
