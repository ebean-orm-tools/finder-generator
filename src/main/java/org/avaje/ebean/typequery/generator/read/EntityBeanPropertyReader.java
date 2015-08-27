package org.avaje.ebean.typequery.generator.read;

import org.avaje.ebean.typequery.generator.GenerationMetaData;
import org.avaje.ebean.typequery.generator.asm.FieldVisitor;
import org.avaje.ebean.typequery.generator.asm.MethodVisitor;
import org.avaje.ebean.typequery.generator.asm.Opcodes;
import org.avaje.ebean.typequery.generator.asm.tree.AnnotationNode;
import org.avaje.ebean.typequery.generator.asm.tree.ClassNode;
import org.avaje.ebean.typequery.generator.asm.tree.FieldNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Read the class meta data using ASM ClassNode.
 */
public class EntityBeanPropertyReader extends ClassNode {

  protected static final Logger logger = LoggerFactory.getLogger(EntityBeanPropertyReader.class);

  public static final String EMBEDDABLE_ANNOTATION= "Ljavax/persistence/Embeddable;";

  public static final String ENTITY_ANNOTATION = "Ljavax/persistence/Entity;";

  public static final String TRANSIENT_ANNOTATION = "Ljavax/persistence/Transient;";

  public static final String ID_ANNOTATION = "Ljavax/persistence/Id;";

  public static final String ENUM = "java/lang/Enum";

  private List<FieldNode> allFields;

  public EntityBeanPropertyReader() {
    super(Opcodes.ASM5);
  }

  @Override
  public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {

    if (ignoreField(access, name)) {
      return null;
    }
    return super.visitField(access, name, desc, signature, value);
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

    // not interested in methods
    return null;
  }

  /**
   * Return the Id field.
   */
  public FieldNode getIdProperty(GenerationMetaData generationMetaData) {

    List<FieldNode> allProperties = getAllProperties(generationMetaData);
    for (FieldNode field : allProperties) {
      if (idField(field)) {
        return field;
      }
    }
    return null;
  }

  /**
   * Return all the fields included fields from inheritance.
   */
  public List<FieldNode> getAllProperties(GenerationMetaData generationMetaData ) {
    if (allFields == null) {
      allFields = new ArrayList<>();
      addClassProperties(allFields, generationMetaData);
    }
    return allFields;
  }

  /**
   * Recursively add properties from the inheritance hierarchy.
   * <p>
   * Includes properties from mapped super classes and usual inheritance.
   * </p>
   */
  protected void addClassProperties(List<FieldNode> allFields, GenerationMetaData generationMetaData ) {

    String superClassName = asDotNotation(superName);
    if (!"java.lang.Object".equals(superClassName)) {
      // look for mappedSuperclass or inheritance etc
      EntityBeanPropertyReader superClass = generationMetaData.getSuperClass(superClassName);
      if (superClass != null) {
        logger.debug("... super type {}", superClassName);
        superClass.addClassProperties(allFields, generationMetaData);
      }
    }

    for (FieldNode field : fields) {
      if (persistentField(field)) {
        allFields.add(field);
      }
    }
  }

  protected String asDotNotation(String path) {
    return path.replace('/', '.');
  }

  /**
   * Return true if it is a persistent field.
   */
  protected boolean persistentField(FieldNode field) {

    // note transient modifier fields are already filtered out
    // along with static fields and ebean added fields
    return !hasAnnotation(field, TRANSIENT_ANNOTATION);
  }

  protected boolean idField(FieldNode field) {
    return hasAnnotation(field, ID_ANNOTATION);
  }

  protected boolean hasAnnotation(FieldNode field, String annotationDesc) {

    // note transient modifier fields are already filtered out
    // along with static fields and ebean added fields

    if (field.visibleAnnotations != null) {
      // look for @Transient annotation
      for (AnnotationNode annotation : field.visibleAnnotations) {
        if (annotation.desc.equals(annotationDesc)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Return true if this is an Enum type.
   */
  public boolean isEnum() {
    return ENUM.equals(superName);
  }

  /**
   * Return true if this is an Embeddable bean.
   */
  public boolean isEmbeddable() {

    if (visibleAnnotations != null) {
      for (AnnotationNode annotation : visibleAnnotations) {
        if (annotation.desc.equals(EMBEDDABLE_ANNOTATION)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Return true if this is an Entity bean.
   */
  public boolean isEntity() {
    if (visibleAnnotations != null) {
      for (AnnotationNode annotation : visibleAnnotations) {
        if (annotation.desc.equals(ENTITY_ANNOTATION)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Not interested in static, transient or Ebean internal fields.
   */
  private boolean ignoreField(int access, String name) {
    return isStaticOrTransient(access) || ignoreEbeanInternalFields(name);
  }

  /**
   * Not interested in static or transient fields.
   */
  private boolean ignoreEbeanInternalFields(String name) {
    return name.startsWith("_ebean") || name.startsWith("_EBEAN");
  }

  /**
   * Not interested in static or transient fields.
   */
  private boolean isStaticOrTransient(int access) {
    return ((access & Opcodes.ACC_STATIC) != 0 || (access & Opcodes.ACC_TRANSIENT) != 0);
  }

}
