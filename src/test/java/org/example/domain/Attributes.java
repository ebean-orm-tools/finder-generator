package org.example.domain;

import javax.persistence.Embeddable;

/**
 * Example embeddable bean.
 */
@Embeddable
public class Attributes {

  String name;

  String description;

  String value;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
