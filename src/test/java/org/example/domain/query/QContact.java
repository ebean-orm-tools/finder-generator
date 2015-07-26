package org.example.domain.query;

import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.TQRootBean;
import org.example.domain.Contact;
import org.example.domain.query.assoc.QAssocContactNote;
import org.example.domain.query.assoc.QAssocCustomer;

public class QContact extends TQRootBean<Contact,QContact> {

  public PString<QContact> firstName;
  public PString<QContact> lastName;
  public PString<QContact> email;
  public PString<QContact> phone;
  public QAssocCustomer<QContact> customer;
  public QAssocContactNote<QContact> notes;

  public QContact() {
    super(Contact.class);
    setRoot(this);
    this.firstName = new PString<>("firstName", this);
    this.lastName = new PString<>("lastName", this);
    this.email = new PString<>("email", this);
    this.phone = new PString<>("phone", this);
    this.customer = new QAssocCustomer<>("customer", this, 5);
    this.notes = new QAssocContactNote<>("notes", this, 5);
  }
}
