package org.example.domain.query;

import io.ebean.EbeanServer;
import io.ebean.typequery.PString;
import io.ebean.typequery.PUuid;
import io.ebean.typequery.TQRootBean;
import io.ebean.typequery.TypeQueryBean;
import org.example.domain.ContactNote;
import org.example.domain.query.assoc.QAssocContact;

/**
 * Query bean for ContactNote.
 * 
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
@TypeQueryBean
public class QContactNote extends TQRootBean<ContactNote,QContactNote> {

  private static final QContactNote _alias = new QContactNote(true);

  /**
   * Return the shared 'Alias' instance used to provide properties to 
   * <code>select()</code> and <code>fetch()</code> 
   */
  public static QContactNote alias() {
    return _alias;
  }

  public PUuid<QContactNote> id;
  public QAssocContact<QContactNote> contact;
  public PString<QContactNote> title;
  public PString<QContactNote> note;


  /**
   * Construct with a given EbeanServer.
   */
  public QContactNote(EbeanServer server) {
    super(ContactNote.class, server);
  }

  /**
   * Construct using the default EbeanServer.
   */
  public QContactNote() {
    super(ContactNote.class);
  }

  /**
   * Construct for Alias.
   */
  private QContactNote(boolean dummy) {
    super(dummy);
  }
}
