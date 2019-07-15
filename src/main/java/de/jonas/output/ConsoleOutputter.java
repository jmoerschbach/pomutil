package de.jonas.output;

import java.util.List;

import de.jonas.analyzer.ProblematicDependency;

public class ConsoleOutputter implements Outputter {

	@Override
	public void output(List<ProblematicDependency> problems) {
		System.out.println("Problems:");
		problems.forEach(System.out::println);
	}

}
