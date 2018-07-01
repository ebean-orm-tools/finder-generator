package org.example.domain.finder;

import io.ebean.Finder;
import org.example.domain.Contact;
import org.example.domain.query.QContact;

public class ContactFinder extends Finder<Long,Contact> {

  /**
   * Construct using the default EbeanServer.
   */
  public ContactFinder() {
    super(Contact.class);
  }


  /**
   * Start a new typed query.
   */
  protected QContact where() {
     return new QContact(db());
  }

  /**
   * Start a new document store query.
   */
  protected QContact text() {
     return new QContact(db()).text();
  }
}
