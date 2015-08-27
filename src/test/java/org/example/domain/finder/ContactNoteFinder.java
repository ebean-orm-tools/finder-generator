package org.example.domain.finder;

import com.avaje.ebean.Finder;
import java.util.UUID;
import org.example.domain.ContactNote;

public class ContactNoteFinder extends Finder<UUID,ContactNote> {

  /**
   * Construct using the default EbeanServer.
   */
  public ContactNoteFinder() {
    super(ContactNote.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public ContactNoteFinder(String serverName) {
    super(serverName, ContactNote.class);
  }
}
