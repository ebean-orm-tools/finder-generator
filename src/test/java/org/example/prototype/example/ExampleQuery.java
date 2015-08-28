package org.example.prototype.example;

import com.avaje.ebean.ExpressionList;
import org.example.BaseTestCase;
import org.example.domain.Contact;
import org.example.domain.Country;
import org.example.domain.Customer;
import org.example.domain.query.QContact;
import org.junit.Test;

/**
 */
public class ExampleQuery extends BaseTestCase {

  @Test
  public void test() {

//    QCustomer qc = new QCustomer();

    Country nz = Country.find.ref("NZ");


    ExpressionList<Contact> filter = new QContact()
        .email.like("some@foo%")
        .getExpressionList();
//
//
//    Customer.find
//        .where()
//        .billingAddress.country.equalTo(nz)
//        //.id.greaterThan(12)
//        //.name.ilike("rob")
//        .order()
//        .id.asc()
//        .contacts.filterMany(filter)
//        //.select("name")
//        //.fetch("contacts", "email")
//          //.query().filterMany("contacts").like("email","rob%@foo.com")
//        //.contacts.filterMany(where)
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
