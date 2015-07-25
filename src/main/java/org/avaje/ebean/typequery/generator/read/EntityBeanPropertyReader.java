package org.avaje.ebean.typequery.generator.read;

import org.avaje.ebean.typequery.generator.asm.FieldVisitor;
import org.avaje.ebean.typequery.generator.asm.MethodVisitor;
import org.avaje.ebean.typequery.generator.asm.Opcodes;
import org.avaje.ebean.typequery.generator.asm.tree.ClassNode;

/**
 * Read the class meta data using ASM ClassNode.
 */
public class EntityBeanPropertyReader extends ClassNode {

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
