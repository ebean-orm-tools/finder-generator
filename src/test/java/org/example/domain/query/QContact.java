package org.example.domain.query;

import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TQRootBean;
import org.avaje.ebean.typequery.TypeQueryBean;
import org.example.domain.Contact;
import org.example.domain.query.assoc.QAssocContactNote;
import org.example.domain.query.assoc.QAssocCustomer;

@TypeQueryBean
public class QContact extends TQRootBean<Contact,QContact> {

  public PLong<QContact> id;
  public PLong<QContact> version;
  public PTimestamp<QContact> whenCreated;
  public PTimestamp<QContact> whenUpdated;
  public PString<QContact> firstName;
  public PString<QContact> lastName;
  public PString<QContact> email;
  public PString<QContact> phone;
  public QAssocCustomer<QContact> customer;
  public QAssocContactNote<QContact> notes;

  public QContact() {
    this(3);
  }
  public QContact(int maxDepth) {
    super(Contact.class);
    setRoot(this);
    this.id = new PLong<>("id", this);
    this.version = new PLong<>("version", this);
    this.whenCreated = new PTimestamp<>("whenCreated", this);
    this.whenUpdated = new PTimestamp<>("whenUpdated", this);
    this.firstName = new PString<>("firstName", this);
    this.lastName = new PString<>("lastName", this);
    this.email = new PString<>("email", this);
    this.phone = new PString<>("phone", this);
    this.customer = new QAssocCustomer<>("customer", this, maxDepth);
    this.notes = new QAssocContactNote<>("notes", this, maxDepth);
  }
}
