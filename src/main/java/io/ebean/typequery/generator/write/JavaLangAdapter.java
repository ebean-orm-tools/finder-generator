package io.ebean.typequery.generator.write;

import java.io.FileWriter;
import java.io.IOException;

public class JavaLangAdapter implements LangAdapter {

	private boolean finderConstructorForNamedServer;

	public JavaLangAdapter() {
		String namedEnv = System.getenv("EBEAN_FINDER_NAMEDSERVER");
		if ("true".equalsIgnoreCase(namedEnv)) {
			finderConstructorForNamedServer = true;
		}
		String namedProp = System.getProperty("finder.namedServer");
		if ("true".equalsIgnoreCase(namedProp)) {
			finderConstructorForNamedServer = true;
		}
	}

	/**
	 * Turn on finder constructor for named servers.
	 */
	public void setFinderConstructorForNamedServer(boolean finderConstructorForNamedServer) {
		this.finderConstructorForNamedServer = finderConstructorForNamedServer;
	}

	@Override
	public void beginClass(FileWriter writer, String shortName) throws IOException {
		writer.append("public class ").append("Q").append(shortName)
				.append(" extends TQRootBean<").append(shortName)
				.append(",Q").append(shortName).append("> {").append(NEWLINE);
	}

	@Override
	public void beginAssocClass(FileWriter writer, String shortName, String origShortName) throws IOException {
		writer.append("public class ").append("Q").append(shortName);
		writer.append("<R> extends TQAssocBean<").append(origShortName).append(",R> {").append(NEWLINE);
	}

	@Override
	public void alias(FileWriter writer, String shortName) throws IOException {
		writer.append("  private static final Q").append(shortName).append(" _alias = new Q");
		writer.append(shortName).append("(true);").append(NEWLINE);
		writer.append(NEWLINE);

		writer.append("  /**").append(NEWLINE);
		writer.append("   * Return the shared 'Alias' instance used to provide properties to ").append(NEWLINE);
		writer.append("   * <code>select()</code> and <code>fetch()</code> ").append(NEWLINE);
		writer.append("   */").append(NEWLINE);
		writer.append("  public static Q").append(shortName).append(" alias() {").append(NEWLINE);
		writer.append("    return _alias;").append(NEWLINE);
		writer.append("  }").append(NEWLINE);
		writer.append(NEWLINE);
	}

	@Override
	public void rootBeanConstructor(FileWriter writer, String shortName) throws IOException {
		writer.append(NEWLINE);
		writer.append("  /**").append(NEWLINE);
		writer.append("   * Construct with a given EbeanServer.").append(NEWLINE);
		writer.append("   */").append(NEWLINE);
		writer.append("  public Q").append(shortName).append("(EbeanServer server) {").append(NEWLINE);
		writer.append("    super(").append(shortName).append(".class, server);").append(NEWLINE);
		writer.append("  }").append(NEWLINE);
		writer.append(NEWLINE);

		writer.append("  /**").append(NEWLINE);
		writer.append("   * Construct using the default EbeanServer.").append(NEWLINE);
		writer.append("   */").append(NEWLINE);
		writer.append("  public Q").append(shortName).append("() {").append(NEWLINE);
		writer.append("    super(").append(shortName).append(".class);").append(NEWLINE);
		writer.append("  }").append(NEWLINE);


		writer.append(NEWLINE);
		writer.append("  /**").append(NEWLINE);
		writer.append("   * Construct for Alias.").append(NEWLINE);
		writer.append("   */").append(NEWLINE);
		writer.append("  private Q").append(shortName).append("(boolean dummy) {").append(NEWLINE);
		writer.append("    super(dummy);").append(NEWLINE);
		writer.append("  }").append(NEWLINE);
	}

	@Override
	public void assocBeanConstructor(FileWriter writer, String shortName) throws IOException {
		writer.append("  public Q").append(shortName).append("(String name, R root) {").append(NEWLINE);
		writer.append("    super(name, root);").append(NEWLINE);
		writer.append("  }").append(NEWLINE);
	}

	@Override
	public void fetch(FileWriter writer, String origShortName) throws IOException {
		writer.append("  /**").append(NEWLINE);
		writer.append("   * Eagerly fetch this association loading the specified properties.").append(NEWLINE);
		writer.append("   */").append(NEWLINE);
		writer.append("  @SafeVarargs").append(NEWLINE);
		writer.append("  public final R fetch(TQProperty<Q").append(origShortName).append(">... properties) {").append(NEWLINE);
		writer.append("    return fetchProperties(properties);").append(NEWLINE);
		writer.append("  }").append(NEWLINE);
		writer.append(NEWLINE);
	}

	@Override
	public void fieldDefn(FileWriter writer, String propertyName, String typeDefn) throws IOException {

		writer.append("  public ");
		writer.append(typeDefn);
		writer.append(" ").append(propertyName).append(";");
	}

	@Override
	public void finderConstructors(FileWriter writer, String shortName) throws IOException {

		writer.append("  /**").append(NEWLINE);
		writer.append("   * Construct using the default EbeanServer.").append(NEWLINE);
		writer.append("   */").append(NEWLINE);
		writer.append("  public ").append(shortName).append("Finder() {").append(NEWLINE);
		writer.append("    super(").append(shortName).append(".class);").append(NEWLINE);
		writer.append("  }").append(NEWLINE);
		writer.append(NEWLINE);
		if (finderConstructorForNamedServer) {
			writer.append("  /**").append(NEWLINE);
			writer.append("   * Construct with a given EbeanServer.").append(NEWLINE);
			writer.append("   */").append(NEWLINE);
			writer.append("  public ").append(shortName).append("Finder(String serverName) {").append(NEWLINE);
			writer.append("    super(").append(shortName).append(".class, serverName);").append(NEWLINE);
			writer.append("  }").append(NEWLINE);
		}
	}

	@Override
	public void finderWhere(FileWriter writer, String shortName, String modifier) throws IOException {
		writer.append(NEWLINE);
		writer.append("  /**").append(NEWLINE);
		writer.append("   * Start a new typed query.").append(NEWLINE);
		writer.append("   */").append(NEWLINE);
		writer.append("  ").append(modifier).append(" Q").append(shortName).append(" where() {").append(NEWLINE);
		writer.append("     return new Q").append(shortName).append("(db());").append(NEWLINE);
		writer.append("  }").append(NEWLINE);

	}

	@Override
	public void finderText(FileWriter writer, String shortName, String modifier) throws IOException {
		writer.append(NEWLINE);
		writer.append("  /**").append(NEWLINE);
		writer.append("   * Start a new document store query.").append(NEWLINE);
		writer.append("   */").append(NEWLINE);
		writer.append("  ").append(modifier).append(" Q").append(shortName).append(" text() {").append(NEWLINE);
		writer.append("     return new Q").append(shortName).append("(db()).text();").append(NEWLINE);
		writer.append("  }").append(NEWLINE);
	}

	@Override
	public void finderClass(FileWriter writer, String shortName, String idTypeShortName) throws IOException {
		writer.append("public class ").append("").append(shortName).append("Finder")
				.append(" extends Finder<").append(idTypeShortName).append(",").append(shortName).append("> {").append(NEWLINE);
	}

	@Override
	public void finderClassEnd(FileWriter writer) throws IOException {
		writer.append("}").append(NEWLINE);
	}

	@Override
	public String finderDefn(String shortName) {
		return shortName + "Finder find = new " + shortName + "Finder();";
	}
}
