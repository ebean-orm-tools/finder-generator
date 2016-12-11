package io.ebean.typequery.generator.write;

import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

public class PropertyTypeMapTest {

  @Test
  public void testGetType() throws Exception {

    PropertyTypeMap map = new PropertyTypeMap();
    PropertyType type = map.getType("java.lang.Boolean");
    String typeDefn = type.getTypeDefn("Customer", false);
    assertEquals("PBoolean<QCustomer>",typeDefn);

    PropertyType instantType = map.getType(Instant.class.getName());
    typeDefn = instantType.getTypeDefn("Customer", false);
    assertEquals("PInstant<QCustomer>",typeDefn);

    PropertyType jodaDateTimeType = map.getType(org.joda.time.DateTime.class.getName());
    typeDefn = jodaDateTimeType.getTypeDefn("Customer", false);
    assertEquals("PJodaDateTime<QCustomer>",typeDefn);

  }
}