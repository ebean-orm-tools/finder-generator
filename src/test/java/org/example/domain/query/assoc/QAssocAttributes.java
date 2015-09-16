package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQAssocBean;
import org.avaje.ebean.typequery.TQProperty;
import org.avaje.ebean.typequery.TypeQueryBean;
import org.example.domain.Attributes;

/**
 * Association query bean for AssocAttributes.
 */
@TypeQueryBean
public class QAssocAttributes<R> extends TQAssocBean<Attributes,R> {

  public PString<R> name;
  public PString<R> description;
  public PString<R> value;

  /**
   * Eagerly fetch this association loading the specified properties.
   */
  @SafeVarargs
  public final R fetch(TQProperty<QAssocAttributes>... properties) {
    return fetchProperties(properties);
  }

  public QAssocAttributes(String name, R root) {
    super(name, root);
  }
}
