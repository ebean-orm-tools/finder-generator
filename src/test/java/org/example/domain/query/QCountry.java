package org.example.domain.query;

import com.avaje.ebean.EbeanServer;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQRootBean;
import org.avaje.ebean.typequery.TypeQueryBean;
import org.example.domain.Country;
import org.example.domain.query.assoc.QAssocAttributes;

@TypeQueryBean
public class QCountry extends TQRootBean<Country,QCountry> {

  public PString<QCountry> code;
  public PString<QCountry> name;
  public QAssocAttributes<QCountry> attributes;

  /**
   * Construct using the default EbeanServer.
   */
  public QCountry() {
    super(Country.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public QCountry(EbeanServer server) {
    super(Country.class, server);
  }
}
