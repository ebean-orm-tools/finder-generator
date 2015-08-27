package org.example.domain.finder;

import com.avaje.ebean.Finder;
import org.example.domain.Country;

public class CountryFinder extends Finder<String,Country> {

  /**
   * Construct using the default EbeanServer.
   */
  public CountryFinder() {
    super(Country.class);
  }

  /**
   * Construct with a given EbeanServer.
   */
  public CountryFinder(String serverName) {
    super(serverName, Country.class);
  }
}
