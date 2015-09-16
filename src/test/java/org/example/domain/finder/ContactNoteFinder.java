package org.example.domain.finder;

import com.avaje.ebean.Finder;
import java.util.UUID;
import org.example.domain.ContactNote;
import org.example.domain.query.QContactNote;

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
    super(ContactNote.class, serverName);
  }

  /**
   * Start a new typed query.
   */
  protected QContactNote where() {
     return new QContactNote(db());
  }
}
