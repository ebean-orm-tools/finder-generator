package org.example.domain.query;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQRootBean;
import org.example.domain.Address;
import org.example.domain.query.assoc.QAssocCountry;

import javax.annotation.Generated;

@Generated(value = "")
public class QAddress extends TQRootBean<Address,QAddress> {

  public PString<QAddress> line1;
  public PString<QAddress> line2;
  public PString<QAddress> city;
  public QAssocCountry<QAddress> country;

  public QAddress() {
    super(Address.class);
    setRoot(this);
    this.line1 = new PString<>("line1", this);
    this.line2 = new PString<>("line2", this);
    this.city = new PString<>("city", this);
    this.country = new QAssocCountry<>("country", this, 3);
  }
}
