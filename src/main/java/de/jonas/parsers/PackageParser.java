package de.jonas.parsers;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PackageParser {

	private List<String> packages;
	private List<File> rootPackages;

//	public PackageParser(String path) {
//		this(new File(path));
//	}

	public PackageParser(List<File> rootPackages) {
		this.rootPackages = rootPackages;
		parse();
	}

	private void parse() {
		List<String> allPackages = new ArrayList<>();
		rootPackages.forEach(p -> visit(p, allPackages));
		this.packages = allPackages.stream().map(s -> s.replace("\\", ".")).collect(Collectors.toList());
		System.out.println("found packages: ");
		packages.forEach(System.out::println);
	}

	public List<String> getPackages() {
		return packages;
	}

	private void visit(File file, List<String> allPackages) {
		if (file.isDirectory())
			allPackages.add(file.getPath());
		File[] children = file.listFiles(f -> f.isDirectory());

		for (int i = 0; i < children.length; i++)
			visit(children[i], allPackages);

	}

}
