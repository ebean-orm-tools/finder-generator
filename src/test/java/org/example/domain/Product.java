package org.example.domain;

import org.example.domain.finder.ProductFinder;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.Month;

/**
 * Product entity bean.
 */
@Entity
@Table(name = "o_product")
public class Product {

  public static final ProductFinder find = new ProductFinder();

  @Id
  protected long id;

  @Size(max = 20)
  String sku;

  String name;

  DateTime jdDateTime;

  Month month;

  /**
   * Return sku.
   */
  public String getSku() {
    return sku;
  }

  /**
   * Set sku.
   */
  public void setSku(String sku) {
    this.sku = sku;
  }

  /**
   * Return name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set name.
   */
  public void setName(String name) {
    this.name = name;
  }


}
