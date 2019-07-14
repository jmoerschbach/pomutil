package de.jonas;

import java.net.URISyntaxException;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import de.jonas.analyzer.DependencyAnalyser;
import de.jonas.analyzer.ProblematicDependency;

public class Main {

	public static void main(String[] args) throws URISyntaxException {
		UserInput input = new UserInput();
		JCommander jcommander = JCommander.newBuilder().addObject(input).build();
		try {
			jcommander.parse(args);
		} catch (ParameterException e) {
			jcommander.usage();
			System.exit(0);
		}
		List<ProblematicDependency> problems = new DependencyAnalyser(input).analyze();
		System.out.println();
		System.out.println("Problems:");
		problems.forEach(System.out::println);

	}

}
