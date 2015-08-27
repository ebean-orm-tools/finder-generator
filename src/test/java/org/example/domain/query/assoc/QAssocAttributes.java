package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TypeQueryBean;

@TypeQueryBean
public class QAssocAttributes<R> {

  public PString<R> name;
  public PString<R> description;
  public PString<R> value;

}
