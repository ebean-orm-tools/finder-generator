package io.ebean.typequery.generator.write;

import io.ebean.typequery.generator.GeneratorConfig;
import io.ebean.typequery.generator.read.EntityBeanPropertyReader;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleFinderLinkWriterTest {

	@Test
	public void sourceLineContainsClassDefn() {

		SimpleFinderLinkWriter linkWriter = new SimpleFinderLinkWriter(new GeneratorConfig(), classMeta("FooBar"));

		assertThat(linkWriter.sourceLineContainsClassDefn("class FooBar ")).isTrue();
		assertThat(linkWriter.sourceLineContainsClassDefn("class FooBar(")).isTrue();
		assertThat(linkWriter.sourceLineContainsClassDefn("class FooBar(val x: Long) : BaseModel()")).isTrue();

		assertThat(linkWriter.sourceLineContainsClassDefn("class FooBarf ")).isFalse();
		assertThat(linkWriter.sourceLineContainsClassDefn("class FooBarf(")).isFalse();
	}

	private EntityBeanPropertyReader classMeta(String name) {
		EntityBeanPropertyReader classMeta = new EntityBeanPropertyReader(null);
		classMeta.name = name;
		return classMeta;
	}
}
