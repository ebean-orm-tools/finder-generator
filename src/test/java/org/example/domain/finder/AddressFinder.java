package org.example.domain.finder;

import com.avaje.ebean.Finder;
import org.example.domain.Address;

public class AddressFinder extends Finder<Long,Address> {

  /**
   * Construct using the default EbeanServer.
   */
  public AddressFinder() {
    super(Address.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public AddressFinder(String serverName) {
    super(serverName, Address.class);
  }
}
