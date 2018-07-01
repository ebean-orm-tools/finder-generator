package org.example.domain.finder;

import io.ebean.Finder;
import org.example.domain.Country;
import org.example.domain.query.QCountry;

public class CountryFinder extends Finder<String,Country> {

  /**
   * Construct using the default EbeanServer.
   */
  public CountryFinder() {
    super(Country.class);
  }


  /**
   * Start a new typed query.
   */
  protected QCountry where() {
     return new QCountry(db());
  }

  /**
   * Start a new document store query.
   */
  protected QCountry text() {
     return new QCountry(db()).text();
  }
}
