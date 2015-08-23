package org.avaje.ebean.typequery.generator;

import org.avaje.ebean.typequery.generator.asm.Type;
import org.avaje.ebean.typequery.generator.asm.tree.AnnotationNode;
import org.avaje.ebean.typequery.generator.asm.tree.FieldNode;
import org.avaje.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.avaje.ebean.typequery.generator.write.PropertyType;
import org.avaje.ebean.typequery.generator.write.PropertyTypeAssoc;
import org.avaje.ebean.typequery.generator.write.PropertyTypeEnum;
import org.avaje.ebean.typequery.generator.write.PropertyTypeMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GenerationMetaData {

  private final PropertyTypeMap propertyMap = new PropertyTypeMap();

  private final Map<String,EntityBeanPropertyReader> entityMap = new LinkedHashMap<>();
  private final Map<String,EntityBeanPropertyReader> embeddedMap = new LinkedHashMap<>();
  private final Map<String,EntityBeanPropertyReader> otherMap = new LinkedHashMap<>();

  private final Map<String,EntityBeanPropertyReader> enumMap = new HashMap<>();

  private final GeneratorConfig config;

  public GenerationMetaData(GeneratorConfig config) {

    this.config = config;
  }

  public PropertyType getPropertyType(FieldNode field, EntityBeanPropertyReader ownerClassMeta) {

    String fieldTypeClassName = Type.getType(field.desc).getClassName();

    PropertyType type = propertyMap.getType(fieldTypeClassName);
    if (type != null) {
      // a known scalar type
      return type;
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
        String propertyName = "QAssoc"+deriveShortName(fieldTypeClassName);
        return new PropertyTypeAssoc(propertyName, config.getAssocPackage());
    }
    String collectParamType = deriveCollectionParameterType(field.signature);
    assocEntity = entityMap.get(collectParamType);
    if (assocEntity != null) {
      String propertyName = "QAssoc"+deriveShortName(collectParamType);
      return new PropertyTypeAssoc(propertyName, config.getAssocPackage());
    }

    return null;
  }

  private String deriveCollectionParameterType(String signature) {
    if (signature == null) {
      return null;
    }
    int typeStart = signature.indexOf("<L");
    if (typeStart == -1) {
      return null;
    }
    return asDotNotation(signature.substring(typeStart+2, signature.length()-3));
  }

  protected String deriveClassNameWithInnerClass(String className) {
    return className.replace('$', '.');
  }

  private String deriveShortName(String className) {
    int startPos = className.lastIndexOf('.');
    if (startPos == -1) {
      return className;
    }
    return className.substring(startPos + 1);
  }

  public void addAll(Collection<EntityBeanPropertyReader> classMetaData) {

    separateTypes(classMetaData);
  }

  protected void separateTypes(Collection<EntityBeanPropertyReader> classMetaData) {

    for (EntityBeanPropertyReader classMeta : classMetaData) {
      if (isEnum(classMeta)) {
        enumMap.put(asDotNotation(classMeta.name), classMeta);
      } else {
        String className = asDotNotation(classMeta.name);
        if (!hasClassAnnotations(classMeta)) {
          otherMap.put(className, classMeta);
        } else if (isEntity(classMeta)) {
          entityMap.put(className, classMeta);
        } else if (isEmbedded(classMeta)) {
          embeddedMap.put(className, classMeta);
        } else {
          otherMap.put(className, classMeta);
        }
      }
    }
  }

  private String asDotNotation(String path) {
    return path.replace('/', '.');
  }


  private boolean hasClassAnnotations(EntityBeanPropertyReader classMeta) {

    List<AnnotationNode> visibleAnnotations = classMeta.visibleAnnotations;
    return (visibleAnnotations != null);
  }

  private boolean isEntity(EntityBeanPropertyReader classMeta) {

    for (AnnotationNode annotation : classMeta.visibleAnnotations) {
      if (annotation.desc.equals("Ljavax/persistence/Entity;")) {
        return true;
      }
    }
    return false;
  }

  private boolean isEmbedded(EntityBeanPropertyReader classMeta) {

    for (AnnotationNode annotation : classMeta.visibleAnnotations) {
      if (annotation.desc.equals("Ljavax/persistence/Embedded;")) {
        return true;
      }
    }
    return false;
  }

  private boolean isEnum(EntityBeanPropertyReader classMeta) {

    return "java/lang/Enum".equals(classMeta.superName);
  }

  public Collection<EntityBeanPropertyReader> getAllEntities() {

    return entityMap.values();
  }

}
