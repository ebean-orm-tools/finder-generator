package io.ebean.typequery.generator.write;


import io.ebean.typequery.generator.GenerationMetaData;
import io.ebean.typequery.generator.GeneratorConfig;
import io.ebean.typequery.generator.asm.tree.FieldNode;
import io.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple implementation that generates and writes query beans.
 */
public class SimpleQueryBeanWriter {

	protected static final Logger logger = LoggerFactory.getLogger(SimpleQueryBeanWriter.class);

	public static final String NEWLINE = "\n";

	protected final GeneratorConfig config;

	protected final EntityBeanPropertyReader classMeta;

	protected final GenerationMetaData generationMetaData;

	protected boolean writingAssocBean;

	protected String destPackage;
	protected String origDestPackage;

	protected String shortName;
	protected String origShortName;

	protected FileWriter writer;

	protected Set<String> importTypes = new TreeSet<>();

	protected List<PropertyMeta> properties = new ArrayList<>();

	public SimpleQueryBeanWriter(GeneratorConfig config, EntityBeanPropertyReader classMeta, GenerationMetaData generationMetaData) {
		this.config = config;
		this.classMeta = classMeta;
		this.generationMetaData = generationMetaData;

		destPackage = config.getDestPackage();
		shortName = deriveShortName(classMeta.name);
	}

	protected void gatherPropertyDetails() {

		importTypes.add(asDotNotation(classMeta.name));
		importTypes.add("io.ebean.typequery.TQRootBean");
		importTypes.add("io.ebean.typequery.TypeQueryBean");
		importTypes.add("io.ebean.EbeanServer");

		addClassProperties(classMeta);
	}

	/**
	 * Recursively add properties from the inheritance hierarchy.
	 * <p>
	 * Includes properties from mapped super classes and usual inheritance.
	 * </p>
	 */
	protected void addClassProperties(EntityBeanPropertyReader classMetaData) {

		List<FieldNode> allProperties = classMetaData.getAllProperties(generationMetaData);
		for (FieldNode field : allProperties) {
			PropertyType type = generationMetaData.getPropertyType(field, classMeta);
			if (type == null) {
				logger.warn("No support for field [" + field.name + "] desc[" + field.desc + "] signature [" + field.signature + "]");
			} else {
				type.addImports(importTypes);
				properties.add(new PropertyMeta(field.name, type));
			}
		}
	}

	/**
	 * Write the type query bean (root bean).
	 */
	public void writeRootBean() throws IOException {

		gatherPropertyDetails();

		if (classMeta.isEntity()) {
			writer = createFileWriter();

			writePackage();
			writeImports();
			writeClass();
			writeAlias();
			writeFields();
			writeConstructors();
			writeClassEnd();

			writer.flush();
			writer.close();
		}
	}

	/**
	 * Write the type query assoc bean.
	 */
	public void writeAssocBean() throws IOException {

		writingAssocBean = true;
		origDestPackage = destPackage;
		destPackage = destPackage + ".assoc";
		origShortName = shortName;
		shortName = "Assoc" + shortName;

		prepareAssocBeanImports();

		writer = createFileWriter();

		writePackage();
		writeImports();
		writeClass();
		writeFields();
		writeConstructors();
		writeClassEnd();

		writer.flush();
		writer.close();
	}

	/**
	 * Prepare the imports for writing assoc bean.
	 */
	protected void prepareAssocBeanImports() {

		importTypes.remove("io.ebean.typequery.TQRootBean");
		importTypes.remove("io.ebean.EbeanServer");
		importTypes.add("io.ebean.typequery.TQAssocBean");
		if (classMeta.isEntity()) {
			importTypes.add("io.ebean.typequery.TQProperty");
			importTypes.add(origDestPackage + ".Q" + origShortName);
		}

		if (!config.isAopStyle()) {
			importTypes.add("io.ebean.typequery.TQPath");
		}

		// remove imports for the same package
		Iterator<String> importsIterator = importTypes.iterator();
		while (importsIterator.hasNext()) {
			String importType = importsIterator.next();
			// there are no subpackages so just use startsWith(destPackage)
			if (importType.startsWith(destPackage)) {
				importsIterator.remove();
			}
		}
	}

	/**
	 * Write constructors.
	 */
	protected void writeConstructors() throws IOException {

		if (writingAssocBean) {
			writeAssocBeanFetch();
			writeAssocBeanConstructor();
		} else {
			writeRootBeanConstructor();
		}
	}

	/**
	 * Write the constructors for 'root' type query bean.
	 */
	protected void writeRootBeanConstructor() throws IOException {

		config.lang().rootBeanConstructor(writer, shortName);


	}

	protected void writeAssocBeanFetch() throws IOException {
		if (classMeta.isEntity()) {
			config.lang().fetch(writer, origShortName);
		}
	}

	/**
	 * Write constructor for 'assoc' type query bean.
	 */
	protected void writeAssocBeanConstructor() throws IOException {
		config.lang().assocBeanConstructor(writer, shortName);
	}

	/**
	 * Return true if this has at least one 'assoc' property.
	 */
	protected boolean hasAssocProperties() {
		for (PropertyMeta property : properties) {
			if (property.isAssociation()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Write all the fields.
	 */
	protected void writeFields() throws IOException {

		for (PropertyMeta property : properties) {
			String typeDefn = property.getTypeDefn(shortName, writingAssocBean);
			config.lang().fieldDefn(writer, property.getName(), typeDefn);
			writer.append(NEWLINE);
		}
		writer.append(NEWLINE);
	}

	/**
	 * Write the class definition.
	 */
	protected void writeClass() throws IOException {

		if (writingAssocBean) {
			writer.append("/**").append(NEWLINE);
			writer.append(" * Association query bean for ").append(shortName).append(".").append(NEWLINE);
			writer.append(" * ").append(NEWLINE);
			writer.append(" * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.").append(NEWLINE);
			writer.append(" */").append(NEWLINE);
			//public class QAssocContact<R>
			writer.append("@TypeQueryBean").append(NEWLINE);
			config.lang().beginAssocClass(writer, shortName, origShortName);

		} else {
			writer.append("/**").append(NEWLINE);
			writer.append(" * Query bean for ").append(shortName).append(".").append(NEWLINE);
			writer.append(" * ").append(NEWLINE);
			writer.append(" * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.").append(NEWLINE);
			writer.append(" */").append(NEWLINE);
			//  public class QContact extends TQRootBean<Contact,QContact> {
			writer.append("@TypeQueryBean").append(NEWLINE);
			config.lang().beginClass(writer, shortName);
		}

		writer.append(NEWLINE);
	}

	protected void writeAlias() throws IOException {
		if (!writingAssocBean) {
			config.lang().alias(writer, shortName);
		}
	}

	protected void writeClassEnd() throws IOException {
		writer.append("}").append(NEWLINE);
	}

	/**
	 * Write all the imports.
	 */
	protected void writeImports() throws IOException {

		for (String importType : importTypes) {
			writer.append("import ").append(importType);
			config.appendLangSemiColon(writer);
			writer.append(NEWLINE);
		}
		writer.append(NEWLINE);
	}

	protected void writePackage() throws IOException {
		writer.append("package ").append(destPackage);
		config.appendLangSemiColon(writer);
		writer.append(NEWLINE).append(NEWLINE);
	}


	protected FileWriter createFileWriter() throws IOException {

		String destDirectory = config.getDestDirectory();
		File destDir = new File(destDirectory);

		String packageAsDir = asSlashNotation(destPackage);

		File packageDir = new File(destDir, packageAsDir);
		if (!packageDir.exists() && !packageDir.mkdirs()) {
			logger.error("Failed to create directory [{}] for generated code", packageDir.getAbsoluteFile());
		}

		String fileName = "Q" + shortName + "." + config.getLang();
		File dest = new File(packageDir, fileName);

		logger.info("writing {}", dest.getAbsolutePath());

		return new FileWriter(dest);
	}

	protected String asDotNotation(String path) {
		return path.replace('/', '.');
	}

	protected String asSlashNotation(String path) {
		return path.replace('.', '/');
	}

	protected String deriveShortName(String name) {
		int startPos = name.lastIndexOf('/');
		if (startPos == -1) {
			return name;
		}
		return name.substring(startPos + 1);
	}

}
