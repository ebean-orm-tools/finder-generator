package org.example.prototype.example;

import org.example.BaseTestCase;
import org.junit.Test;

/**
 */
public class MainMethodTest extends BaseTestCase {


  public static void main(String[] args) {

    System.out.println("MainMethodTest start...");
    ExampleQuery query = new ExampleQuery();
    System.out.println("MainMethodTest run test ...");
    query.test();

  }

  @Test
  public void test() {
    // place holder
  }
}
