package io.ebean.typequery.generator.write;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class KotlinLangAdapter implements LangAdapter {

  @Override
  public void beginClass(FileWriter writer, String shortName) throws IOException {
    //class QCountry : TQRootBean<Country, QCountry> {
    writer.append("class ").append("Q").append(shortName)
      .append(" : TQRootBean<").append(shortName)
      .append(", Q").append(shortName).append("> {").append(NEWLINE);
  }

  @Override
  public void beginAssocClass(FileWriter writer, String shortName, String origShortName) throws IOException {
    writer.append("class ").append("Q").append(shortName);
    writer.append("<R>(name: String, root: R) : TQAssocBean<").append(origShortName).append(",R>(name, root) {").append(NEWLINE);
  }

  @Override
  public void alias(FileWriter writer, String shortName) throws IOException {

    writer.append("  companion object {").append(NEWLINE);
    writer.append("    /**").append(NEWLINE);
    writer.append("     * shared 'Alias' instance used to provide").append(NEWLINE);
    writer.append("     * properties to select and fetch clauses").append(NEWLINE);
    writer.append("     */").append(NEWLINE);
    writer.append("    val _alias = Q").append(shortName).append("(true)").append(NEWLINE);
    writer.append("  }").append(NEWLINE).append(NEWLINE);
  }

  @Override
  public void assocBeanConstructor(FileWriter writer, String shortName) {
    // not required for kotlin as part of type definition
  }

  @Override
  public void fetch(FileWriter writer, String origShortName) throws IOException {

      writeAssocBeanFetch(writer, origShortName, "", "Eagerly fetch this association loading the specified properties.");
      writeAssocBeanFetch(writer, origShortName, "Query", "Eagerly fetch this association using a 'query join' loading the specified properties.");
      writeAssocBeanFetch(writer, origShortName, "Lazy", "Use lazy loading for this association loading the specified properties.");
  }

  private void writeAssocBeanFetch(Writer writer, String origShortName, String fetchType, String comment) throws IOException {

//    fun fetch(vararg properties: TQProperty<QContact>): R {
//      return fetchProperties(*properties)
//    }

    writer.append("  /**").append(NEWLINE);
    writer.append("   * ").append(comment).append(NEWLINE);
    writer.append("   */").append(NEWLINE);
    writer.append("  fun fetch").append(fetchType).append("(vararg properties: TQProperty<Q").append(origShortName).append(">) : R {").append(NEWLINE);
    writer.append("    return fetch").append(fetchType).append("Properties(*properties)").append(NEWLINE);
    writer.append("  }").append(NEWLINE);
    writer.append(NEWLINE);
  }

  @Override
  public void rootBeanConstructor(FileWriter writer, String shortName) throws IOException {

    writer.append(NEWLINE);
    writer.append("  /**").append(NEWLINE);
    writer.append("   * Construct with a given Database.").append(NEWLINE);
    writer.append("   */").append(NEWLINE);
    writer.append("  constructor(database: Database) : super(").append(shortName).append("::class.java, database)");
    writer.append(NEWLINE);
    writer.append(NEWLINE);

    writer.append("  /**").append(NEWLINE);
    writer.append("   * Construct using the default Database.").append(NEWLINE);
    writer.append("   */").append(NEWLINE);
    writer.append("  constructor() : super(").append(shortName).append("::class.java)");
    writer.append(NEWLINE);

    writer.append(NEWLINE);
    writer.append("  /**").append(NEWLINE);
    writer.append("   * Construct for Alias.").append(NEWLINE);
    writer.append("   */").append(NEWLINE);
    writer.append("  private constructor(dummy: Boolean) : super(dummy)").append(NEWLINE);
  }

  @Override
  public void fieldDefn(FileWriter writer, String propertyName, String typeDefn) throws IOException {

    writer.append("  lateinit var ");
    writer.append(propertyName).append(": ");
    writer.append(typeDefn);
  }

  @Override
  public void finderConstructors(FileWriter writer, String shortName) {
    // nothing here now
  }

  @Override
  public void finderWhere(FileWriter writer, String shortName, String modifier) throws IOException {
    writer.append(NEWLINE);
    writer.append("  /**").append(NEWLINE);
    writer.append("   * Start a new typed query.").append(NEWLINE);
    writer.append("   */").append(NEWLINE);
    writer.append("  fun where(): Q").append(shortName).append(" {").append(NEWLINE);
    writer.append("     return Q").append(shortName).append("(db())").append(NEWLINE);
    writer.append("  }").append(NEWLINE);
  }

  @Override
  public void finderText(FileWriter writer, String shortName, String modifier) throws IOException {
    writer.append(NEWLINE);
    writer.append("  /**").append(NEWLINE);
    writer.append("   * Start a new document store query.").append(NEWLINE);
    writer.append("   */").append(NEWLINE);
    writer.append("  fun text(): Q").append(shortName).append(" {").append(NEWLINE);
    writer.append("     return Q").append(shortName).append("(db()).text()").append(NEWLINE);
    writer.append("  }").append(NEWLINE);
  }

  @Override
  public void finderClass(FileWriter writer, String shortName, String idTypeShortName) throws IOException {

    //open class AddressFinder : Finder<Long, Address>(Address::class.java)

    writer.append("open class ").append("").append(shortName).append("Finder")
      .append(" : Finder<").append(idTypeShortName).append(", ")
      .append(shortName).append(">(").append(shortName).append("::class.java)")
      .append(NEWLINE);
  }

  @Override
  public void finderClassEnd(FileWriter writer) {
    // do nothing
  }

  @Override
  public String finderDefn(String shortName) {
    return "companion object Find : " + shortName + "Finder()";
  }
}
