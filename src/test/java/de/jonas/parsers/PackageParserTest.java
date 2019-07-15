package de.jonas.parsers;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PackageParserTest {

	private PackageParser toTest;

	@Before
	public void setUp() throws Exception {
		toTest = new PackageParser(new File("src/main/java/de/jonas"));
	}

	@Test
	public void testGetPackages() {
		assertEquals(Arrays.asList("src.main.java.de.jonas","src.main.java.de.jonas.analyzer", "src.main.java.de.jonas.output","src.main.java.de.jonas.parsers"), toTest.getPackages());
	}

}
