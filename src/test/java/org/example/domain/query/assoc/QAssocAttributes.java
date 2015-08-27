package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQAssocBean;
import org.avaje.ebean.typequery.TypeQueryBean;
import org.example.domain.Attributes;

@TypeQueryBean
public class QAssocAttributes<R> extends TQAssocBean<Attributes,R> {

  public PString<R> name;
  public PString<R> description;
  public PString<R> value;

  public QAssocAttributes(String name, R root) {
    super(name, root);
  }
}
