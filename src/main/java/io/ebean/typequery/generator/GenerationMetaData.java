package io.ebean.typequery.generator;

import io.ebean.typequery.generator.read.EntityBeanPropertyReader;
import io.ebean.typequery.generator.write.PropertyType;
import io.ebean.typequery.generator.write.PropertyTypeAssoc;
import io.ebean.typequery.generator.write.PropertyTypeEnum;
import io.ebean.typequery.generator.write.PropertyTypeMap;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Meta data gathered on the beans as part of the generation.
 */
public class GenerationMetaData {

  protected final PropertyTypeMap propertyMap = new PropertyTypeMap();

  protected final Map<String, EntityBeanPropertyReader> entityMap = new LinkedHashMap<>();

  protected final Map<String, EntityBeanPropertyReader> otherMap = new LinkedHashMap<>();

  protected final Map<String, EntityBeanPropertyReader> enumMap = new HashMap<>();

  protected final GeneratorConfig config;

  public GenerationMetaData(GeneratorConfig config) {
    this.config = config;
  }

  /**
   * Return the set of entities loaded.
   */
  public Set<String> getLoadedEntities() {
    return entityMap.keySet();
  }

  /**
   * Return the meta data for the given super class.
   * <p>
   * This is used to read inherited properties from mapped super class and inheritance hierarchies.
   * </p>
   */
  public EntityBeanPropertyReader getSuperClass(String superClassName) {
    EntityBeanPropertyReader superMeta = otherMap.get(superClassName);
    if (superMeta == null) {
      superMeta = entityMap.get(superClassName);
    }
    return superMeta;
  }

  public PropertyType getPropertyType(FieldNode field, EntityBeanPropertyReader ownerClassMeta) {

    String fieldTypeClassName = Type.getType(field.desc).getClassName();

    PropertyType type = propertyMap.getType(fieldTypeClassName);
    if (type != null) {
      // a known scalar type
      return type;
    }

    if (EntityBeanPropertyReader.dbJsonField(field)) {
      return propertyMap.getDbJsonType();
    }

    EntityBeanPropertyReader anEnum = enumMap.get(fieldTypeClassName);
    if (anEnum != null) {
      // change inner class $ to a period
      fieldTypeClassName = deriveClassNameWithInnerClass(fieldTypeClassName);
      return new PropertyTypeEnum(fieldTypeClassName, deriveShortName(fieldTypeClassName));
    }

    // change inner class $ to a period
    fieldTypeClassName = deriveClassNameWithInnerClass(fieldTypeClassName);

    EntityBeanPropertyReader assocEntity = entityMap.get(fieldTypeClassName);
    if (assocEntity != null) {
      //  public QAssocContact<QCustomer> contacts;
      String propertyName = "QAssoc" + deriveShortName(fieldTypeClassName);
      return new PropertyTypeAssoc(propertyName, config.getAssocPackage());
    }
    String collectParamType = deriveCollectionParameterType(field.signature);
    assocEntity = entityMap.get(collectParamType);
    if (assocEntity != null) {
      String propertyName = "QAssoc" + deriveShortName(collectParamType);
      return new PropertyTypeAssoc(propertyName, config.getAssocPackage());
    }

    return null;
  }

  protected String deriveCollectionParameterType(String signature) {
    if (signature == null) {
      return null;
    }
    int typeStart = signature.indexOf("<L");
    if (typeStart == -1) {
      return null;
    }
    return asDotNotation(signature.substring(typeStart + 2, signature.length() - 3));
  }

  protected String deriveClassNameWithInnerClass(String className) {
    return className.replace('$', '.');
  }

  protected String deriveShortName(String className) {
    int startPos = className.lastIndexOf('.');
    if (startPos == -1) {
      return className;
    }
    return className.substring(startPos + 1);
  }

  /**
   * Add all the class metadata based on the type (Enum/Entity etc).
   */
  public void addAll(Collection<EntityBeanPropertyReader> classMetaData) {

    for (EntityBeanPropertyReader classMeta : classMetaData) {
      addClassMeta(classMeta);
    }
  }

  /**
   * Add the class metadata based on the type (Enum/Entity etc).
   */
  public void addClassMeta(EntityBeanPropertyReader classMeta) {
    String className = asDotNotation(classMeta.name);
    if (classMeta.isEnum()) {
      enumMap.put(className, classMeta);
    } else {
      if (classMeta.isEntity() || classMeta.isEmbeddable()) {
        entityMap.put(className, classMeta);
      } else {
        otherMap.put(className, classMeta);
      }
    }
  }

  protected String asDotNotation(String path) {
    return path.replace('/', '.');
  }

  protected Collection<EntityBeanPropertyReader> getAllEntities() {
    return entityMap.values();
  }

}
