package de.jonas.analyzer;

import org.junit.Before;
import org.junit.Test;

import de.jonas.UserInput;

public class DependencyAnalyzerTest {

	DependencyAnalyser toTest;

	@Before
	public void setUp() throws Exception {
		UserInput input = new UserInput();
		toTest = new DependencyAnalyser(input);
	}

	@Test
	public void testAnalyze() {
	}

}
