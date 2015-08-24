package org.example.domain.query;

import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TQRootBean;
import org.example.domain.ContactNote;
import org.example.domain.query.assoc.QAssocContact;

public class QContactNote extends TQRootBean<ContactNote,QContactNote> {

  public PLong<QContactNote> id;
  public PLong<QContactNote> version;
  public PTimestamp<QContactNote> whenCreated;
  public PTimestamp<QContactNote> whenUpdated;
  public QAssocContact<QContactNote> contact;
  public PString<QContactNote> title;
  public PString<QContactNote> note;

  public QContactNote() {
    this(3);
  }
  public QContactNote(int maxDepth) {
    super(ContactNote.class);
    setRoot(this);
    this.id = new PLong<>("id", this);
    this.version = new PLong<>("version", this);
    this.whenCreated = new PTimestamp<>("whenCreated", this);
    this.whenUpdated = new PTimestamp<>("whenUpdated", this);
    this.title = new PString<>("title", this);
    this.note = new PString<>("note", this);
    this.contact = new QAssocContact<>("contact", this, maxDepth);
  }
}
