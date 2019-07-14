package de.jonas.parsers;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ImportParserTest {

	private ImportParser toTest;

	@Before
	public void setUp() throws Exception {
		toTest = new ImportParser(new File("src/test/resources/Database.java"));
	}

	@Test
	public void testGetAllImportStatements() {
		List<String> expected = Arrays.asList("java.util.stream.Collectors.toList;", "java.util.ArrayList;",
				"java.util.Arrays;", "java.util.HashMap;", "java.util.List;", "java.util.Map;", "java.util.Set;",
				"java.util.stream.Collectors;", "org.reflections.Reflections;",
				"org.reflections.scanners.SubTypesScanner;", "org.reflections.scanners.TypeAnnotationsScanner;",
				"org.reflections.util.ClasspathHelper;", "org.reflections.util.ConfigurationBuilder;",
				"org.reflections.util.FilterBuilder;", "aventurian.Race;", "skills.InstantiableSkill;",
				"skills.attributes.primary.PrimaryAttribute;", "skills.attributes.secondary.SecondaryAttribute;",
				"skills.languages.Language;", "skills.properties.BadProperty;", "skills.properties.Property;");
		assertEquals(expected, toTest.getAllImportStatements());
	}

	@Test
	public void testGetNonJavaImportStatements() {
		List<String> expected = Arrays.asList("org.reflections.Reflections;",
				"org.reflections.scanners.SubTypesScanner;", "org.reflections.scanners.TypeAnnotationsScanner;",
				"org.reflections.util.ClasspathHelper;", "org.reflections.util.ConfigurationBuilder;",
				"org.reflections.util.FilterBuilder;", "aventurian.Race;", "skills.InstantiableSkill;",
				"skills.attributes.primary.PrimaryAttribute;", "skills.attributes.secondary.SecondaryAttribute;",
				"skills.languages.Language;", "skills.properties.BadProperty;", "skills.properties.Property;");
		assertEquals(expected, toTest.getNonJavaImportStatements());
	}

}
