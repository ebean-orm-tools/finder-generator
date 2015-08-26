package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TypeQueryBean;

@TypeQueryBean
public class QAssocCountry<R> {

  public PString<R> code;
  public PString<R> name;

}
