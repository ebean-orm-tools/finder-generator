package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PUuid;
import org.avaje.ebean.typequery.TQAssocBean;
import org.avaje.ebean.typequery.TypeQueryBean;
import org.example.domain.ContactNote;

@TypeQueryBean
public class QAssocContactNote<R> extends TQAssocBean<ContactNote,R> {

  public PUuid<R> id;
  public QAssocContact<R> contact;
  public PString<R> title;
  public PString<R> note;

  public QAssocContactNote(String name, R root) {
    super(name, root);
  }
}
