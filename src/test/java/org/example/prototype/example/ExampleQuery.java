package org.example.prototype.example;

import org.example.domain.Country;
import org.example.domain.Customer;
import org.example.domain.query.QCustomer;
import org.junit.Test;

/**
 */
public class ExampleQuery {

  @Test
  public void test() {

//    QCustomer qc = new QCustomer();

//    Country nz = Country.find.ref("NZ");


//    Customer.find
//        .where()
//        .billingAddress.country.code.equalTo(nz.getCode())
//        .id.greaterThan(12)
//        .name.ilike("rob")
//        .order()
//        .id.asc()
//        .findList();

//    Customer rob = new QCustomer()
//        .select("id, name")
//        .id.greaterThan(42)
//        .status.equalTo(Customer.Status.GOOD)
//        .name.ilike("Asd")
//        .name.isNull()
//        .billingAddress.country.code.equalTo("NZ")
//        .contacts.email.endsWith("@foo.com")
//        .findUnique();

  }

  @Test
  public void testOrder() {

//    List<Order> orders = new QOrder()
//        .customer.name.ilike("rob")
//        .orderBy()
//        .customer.name.asc()
//        .orderDate.asc()
//        .findList();

//        .shippingAddress.city.ieq("auckla")
//        .shippingAddress.country.code.eq("NZ")
//        .status.equalTo(Order.Status.APPROVED)
//        .orderDate.after(Date.valueOf("2015-01-20"))
//        .findList();
  }

}
