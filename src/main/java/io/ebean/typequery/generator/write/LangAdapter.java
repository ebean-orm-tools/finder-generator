package io.ebean.typequery.generator.write;

import java.io.FileWriter;
import java.io.IOException;

public interface LangAdapter {

	String NEWLINE = "\n";

	void beginClass(FileWriter writer, String shortName) throws IOException;

	void beginAssocClass(FileWriter writer, String shortName, String origShortName) throws IOException;

	void alias(FileWriter writer, String shortName) throws IOException;

	void rootBeanConstructor(FileWriter writer, String shortName) throws IOException;

	void assocBeanConstructor(FileWriter writer, String shortName) throws IOException;

	void fetch(FileWriter writer, String origShortName) throws IOException;

	void fieldDefn(FileWriter writer, String propertyName, String typeDefn) throws IOException;

	void finderConstructors(FileWriter writer, String shortName) throws IOException;

	void finderWhere(FileWriter writer, String shortName, String modifier) throws IOException;

	void finderText(FileWriter writer, String shortName, String modifier) throws IOException;

	void finderClass(FileWriter writer, String shortName, String idTypeShortName) throws IOException;

	void finderClassEnd(FileWriter writer) throws IOException;

	String finderDefn(String shortName);

}
