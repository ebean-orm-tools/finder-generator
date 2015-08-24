package org.example.domain.query;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQRootBean;
import org.avaje.ebean.typequery.TypeQueryBean;
import org.example.domain.Country;

@TypeQueryBean
public class QCountry extends TQRootBean<Country,QCountry> {

  public PString<QCountry> code;
  public PString<QCountry> name;

  public QCountry() {
    this(3);
  }
  public QCountry(int maxDepth) {
    super(Country.class);
    setRoot(this);
    this.code = new PString<>("code", this);
    this.name = new PString<>("name", this);
  }
}
