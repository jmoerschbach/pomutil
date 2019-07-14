package de.jonas.parsers;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.jonas.parsers.MavenParser;

public class MavenParserTest {

	private MavenParser toTest;

	@Before
	public void setup() {
		toTest = new MavenParser(new File("src/test/resources/sampleDependencyTree.txt"));

	}

	@Test
	public void testGetDirectDependencies() {
		assertEquals(Arrays.asList("org.mockito", "junit", "org.openjfx", "org.testfx", "org.reflections"),
				toTest.getDirectDependencies());
	}

	@Test
	public void testGetModule() {
		assertEquals("DSA_Helden_Tool_v2", toTest.getModule());
	}

}
