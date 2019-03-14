package org.example.domain.query;

import io.ebean.Database;
import io.ebean.typequery.PString;
import io.ebean.typequery.TQRootBean;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.Country;
import org.example.domain.query.assoc.QAssocAttributes;

/**
 * Query bean for Country.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QCountry extends TQRootBean<Country,QCountry> {

  private static final QCountry _alias = new QCountry(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QCountry alias() {
    return _alias;
  }

  public PString<QCountry> code;
  public PString<QCountry> name;
  public QAssocAttributes<QCountry> attributes;


  /**
   * Construct with a given Database.
   */
  public QCountry(Database database) {
    super(Country.class, database);
  }

  /**
   * Construct using the default Database.
   */
  public QCountry() {
    super(Country.class);
  }

  /**
   * Construct for Alias.
   */
  private QCountry(boolean dummy) {
    super(dummy);
  }
}
