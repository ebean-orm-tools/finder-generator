package org.example.domain.query;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQRootBean;
import org.example.domain.ContactNote;
import org.example.domain.query.assoc.QAssocContact;

public class QContactNote extends TQRootBean<ContactNote,QContactNote> {

  public QAssocContact<QContactNote> contact;
  public PString<QContactNote> title;
  public PString<QContactNote> note;

  public QContactNote() {
    super(ContactNote.class);
    setRoot(this);
    this.title = new PString<>("title", this);
    this.note = new PString<>("note", this);
    this.contact = new QAssocContact<>("contact", this, 5);
  }
}
