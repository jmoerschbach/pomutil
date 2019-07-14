package de.jonas.parsers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PackageParser {

	private File root;
	private List<String> packages;

	public PackageParser(String path) {
		this(new File(path));
	}

	public PackageParser(File file) {
		this.root = file;
		parse();
	}

	private void parse() {
		List<String> allPackages = new ArrayList<>();
		visit(root, allPackages);
		this.packages = allPackages.stream().map(s -> s.replace("\\", ".")).collect(Collectors.toList());
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
