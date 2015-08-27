package org.example.domain;

import org.example.domain.finder.ContactNoteFinder;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class ContactNote {

  public static final ContactNoteFinder find = new ContactNoteFinder();


  @Id
  UUID id;

  @ManyToOne(optional = false)
  Contact contact;

  String title;

  @Lob
  String note;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

}
