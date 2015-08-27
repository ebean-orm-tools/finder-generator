package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PUuid;
import org.avaje.ebean.typequery.TypeQueryBean;

@TypeQueryBean
public class QAssocContactNote<R> {

  public PUuid<R> id;
  public QAssocContact<R> contact;
  public PString<R> title;
  public PString<R> note;

}
