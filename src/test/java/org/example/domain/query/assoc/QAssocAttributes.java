package org.example.domain.query.assoc;

import io.ebean.typequery.PString;
import io.ebean.typequery.TQAssocBean;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.Attributes;

/**
 * Association query bean for AssocAttributes.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QAssocAttributes<R> extends TQAssocBean<Attributes,R> {

  public PString<R> name;
  public PString<R> description;
  public PString<R> value;

  public QAssocAttributes(String name, R root) {
    super(name, root);
  }
}
