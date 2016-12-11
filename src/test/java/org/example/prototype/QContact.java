package org.example.prototype;

import io.ebean.typequery.PLong;
import io.ebean.typequery.PString;
import io.ebean.typequery.TQRootBean;
import org.example.domain.Contact;
import org.example.prototype.assoc.QAssocContactNote;

public class QContact extends TQRootBean<Contact,QContact> {

  public PLong<QContact> id;

  public PString<QContact> name;

  public PString<QContact> email;

  public QAssocContactNote<QContact> notes;

  public QContact() {
    super(Contact.class);
    setRoot(this);
    this.id = new PLong<>("id", this);
    this.name = new PString<>("name", this);
    this.email = new PString<>("email", this);
    this.notes = new QAssocContactNote<>("notes", this, 5);
  }

}
