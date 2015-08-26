package org.example.domain.query.assoc;

import org.avaje.ebean.typequery.PLong;
import org.avaje.ebean.typequery.PString;
import org.avaje.ebean.typequery.PTimestamp;
import org.avaje.ebean.typequery.TypeQueryBean;

@TypeQueryBean
public class QAssocContact<R> {

  public PLong<R> id;
  public PLong<R> version;
  public PTimestamp<R> whenCreated;
  public PTimestamp<R> whenUpdated;
  public PString<R> firstName;
  public PString<R> lastName;
  public PString<R> email;
  public PString<R> phone;
  public QAssocCustomer<R> customer;
  public QAssocContactNote<R> notes;

}
