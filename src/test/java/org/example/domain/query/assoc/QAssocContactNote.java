package org.example.domain.query.assoc;

import io.ebean.typequery.PString;
import io.ebean.typequery.PUuid;
import io.ebean.typequery.TQAssocBean;
import io.ebean.typequery.TQProperty;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.ContactNote;
import org.example.domain.query.QContactNote;

/**
 * Association query bean for AssocContactNote.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QAssocContactNote<R> extends TQAssocBean<ContactNote,R> {

  public PUuid<R> id;
  public QAssocContact<R> contact;
  public PString<R> title;
  public PString<R> note;

  /**
   * Eagerly fetch this association loading the specified properties.
   */
  @SafeVarargs
  public final R fetch(TQProperty<QContactNote>... properties) {
    return fetchProperties(properties);
  }

  public QAssocContactNote(String name, R root) {
    super(name, root);
  }
}
