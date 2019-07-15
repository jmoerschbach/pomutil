package de.jonas;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import de.jonas.analyzer.DependencyAnalyser;
import de.jonas.analyzer.ProblematicDependency;
import de.jonas.output.ConsoleOutputter;
import de.jonas.output.FileOutputter;
import de.jonas.output.Outputter;

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
		Outputter console = new ConsoleOutputter();
		console.output(problems);

		Outputter file = new FileOutputter(new File(input.getOutputFile()));
		file.output(problems);

	}

}
