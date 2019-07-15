package de.jonas.analyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProblematicDependency {

	private List<String> imports;
	private File file;

	public ProblematicDependency(File f, List<String> imports) {
		this.file = f;
		this.imports = imports;
	}

	public String toString() {
		return file.toString() + ": " + imports;
	}

	public String getFileName() {
		return file.getPath();
	}

	public List<String> getProblematicStatements() {
		return new ArrayList<String>(imports);
	}

}
