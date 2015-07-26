package org.avaje.ebean.typequery.generator.write;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class PropertyTypeMap {

  Map<String,PropertyType> map = new HashMap<>();

  public PropertyTypeMap() {
    map.put("boolean", new PropertyType("PBoolean"));
    map.put("int", new PropertyType("PInteger"));
    map.put("long", new PropertyType("PLong"));
    map.put(String.class.getName(), new PropertyType("PString"));
    map.put(Long.class.getName(), new PropertyType("PLong"));
    map.put(Integer.class.getName(), new PropertyType("PInteger"));
    map.put(Double.class.getName(), new PropertyType("PDouble"));
    map.put(Timestamp.class.getName(), new PropertyType("PTimestamp"));
    map.put(java.sql.Date.class.getName(), new PropertyType("PSqlDate"));
    map.put(java.util.Date.class.getName(), new PropertyType("PUtilDate"));
  }

  public PropertyType getType(String classDesc) {

    return map.get(classDesc);
  }
}
