package io.ebean.typequery.generator.read;

import io.ebean.typequery.generator.GenerationMetaData;
import org.objectweb.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Read a class file and return the EntityBeanPropertyReader holding the meta data on the class.
 */
public class MetaClassFileReader {

  private final GenerationMetaData generationMetaData;

  public MetaClassFileReader(GenerationMetaData generationMetaData) {
    this.generationMetaData = generationMetaData;
  }

  /**
   * Read a class classFile and return the EntityBeanPropertyReader.
   */
  public EntityBeanPropertyReader readClassViaClassPath(String className) throws IOException {

    ClassLoader classLoader = this.getClass().getClassLoader();
    InputStream is = classLoader.getResourceAsStream(className.replace('.', '/') + ".class");
    if (is == null) {
      throw new IOException("No resource for " + className);
    }
    try {
      ClassReader cr2 = new ClassReader(is);
      EntityBeanPropertyReader classNode = new EntityBeanPropertyReader(generationMetaData);
      cr2.accept(classNode, 0);
      return classNode;

    } finally {
      is.close();
    }
  }

  /**
   * Read a class classFile and return the EntityBeanPropertyReader.
   */
  public EntityBeanPropertyReader readClassFile(File classFile) throws IOException {

    byte[] classfileBuffer = readBytes(classFile);

    ClassReader cr2 = new ClassReader(classfileBuffer);
    EntityBeanPropertyReader classNode = new EntityBeanPropertyReader(generationMetaData);

    //ClassNode is a ClassVisitor
    cr2.accept(classNode, 0);

    return classNode;
  }

  private byte[] readBytes(File file) throws IOException {

    FileInputStream fis = new FileInputStream(file);
    try {
      return readBytes(fis);
    } finally {
      fis.close();
    }
  }

  private byte[] readBytes(InputStream is) throws IOException {

    BufferedInputStream bis = new BufferedInputStream(is);
    ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);

    byte[] buf = new byte[1028];

    int len;
    while ((len = bis.read(buf, 0, buf.length)) > -1) {
      baos.write(buf, 0, len);
    }
    baos.flush();
    baos.close();
    return baos.toByteArray();
  }


}
