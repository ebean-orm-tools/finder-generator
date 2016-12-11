package org.example.domain.query.assoc;

import io.ebean.typequery.PString;
import io.ebean.typequery.TQAssocBean;
import io.ebean.typequery.TQProperty;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.Country;
import org.example.domain.query.QCountry;

/**
 * Association query bean for AssocCountry.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QAssocCountry<R> extends TQAssocBean<Country,R> {

  public PString<R> code;
  public PString<R> name;
  public QAssocAttributes<R> attributes;

  /**
   * Eagerly fetch this association loading the specified properties.
   */
  @SafeVarargs
  public final R fetch(TQProperty<QCountry>... properties) {
    return fetchProperties(properties);
  }

  public QAssocCountry(String name, R root) {
    super(name, root);
  }
}
