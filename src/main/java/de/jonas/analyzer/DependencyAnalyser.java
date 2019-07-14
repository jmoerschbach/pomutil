package de.jonas.analyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.jonas.UserInput;
import de.jonas.parsers.ImportParser;
import de.jonas.parsers.MavenParser;
import de.jonas.parsers.PackageParser;

public class DependencyAnalyser {

	private List<String> directDependencies;
	private List<String> packages;
	private UserInput input;

	public DependencyAnalyser(UserInput input) {
		this.input = input;
		directDependencies = new MavenParser(input.getMavenDepencencyTree()).getDirectDependencies();
		packages = new PackageParser(new File(input.getRootPackage())).getPackages();

	}

	public List<ProblematicDependency> analyze() {
		File f = new File(input.getRootPackage());
		System.out.println("start analyzing " + f);
		List<ProblematicDependency> problems = new ArrayList<>();
		visit(f, problems);
		return problems;
	}

	private void visit(File file, List<ProblematicDependency> problems) {
		if (file.isFile()) {
			if (file.getPath().endsWith(".java"))
				analyzeFile(file, problems);
		} else {
			File[] children = file.listFiles();

			for (int i = 0; i < children.length; i++)
				visit(children[i], problems);
		}
	}

	private void analyzeFile(File file, List<ProblematicDependency> problems) {
		System.out.println("analyzing " + file);
		ImportParser i = new ImportParser(file);
		List<String> imports = i.getNonJavaImportStatements();

		imports.removeIf(this::isInPackages);
		imports.removeIf(this::isInDirectDependencies);
		if (!imports.isEmpty()) {
			System.out.println("found problematic imports: " + imports);
			problems.add(new ProblematicDependency(file, imports));
		}
	}

	private boolean isInPackages(String importStatement) {
		int lastPoint = importStatement.lastIndexOf(".", importStatement.length());
		importStatement = importStatement.substring(0, lastPoint);
		for (String currentPackage : packages) {
			if (currentPackage.contains(importStatement))
				return true;
		}
		return false;
	}

	private boolean isInDirectDependencies(String importStatement) {
		int lastPoint = importStatement.lastIndexOf(".", importStatement.length());
		importStatement = importStatement.substring(0, lastPoint);
		for (String currentPackage : directDependencies) {
			if (importStatement.startsWith(currentPackage))
				return true;
		}
		return false;
	}

}
