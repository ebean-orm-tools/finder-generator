package org.example.domain.finder;

import com.avaje.ebean.Finder;
import org.example.domain.Contact;

public class ContactFinder extends Finder<Long,Contact> {

  /**
   * Construct using the default EbeanServer.
   */
  public ContactFinder() {
    super(Contact.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public ContactFinder(String serverName) {
    super(serverName, Contact.class);
  }
}
