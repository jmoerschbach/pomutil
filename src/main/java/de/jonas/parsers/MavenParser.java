package de.jonas.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.dutra.tools.maven.deptree.core.InputType;
import fr.dutra.tools.maven.deptree.core.Node;
import fr.dutra.tools.maven.deptree.core.ParseException;
import fr.dutra.tools.maven.deptree.core.Parser;

public class MavenParser {

	private File toParse;
	private List<String> directDependencies;
	private String module;

	public MavenParser(String file) {
		this(new File(file));
	}

	public MavenParser(File file) {
		this.toParse = file;
		parse();
	}

	private void parse() {
		List<String> all = new ArrayList<>();
		try (Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(toParse), "UTF-8"))) {
			Parser parser = InputType.TEXT.newParser();
			Node tree = parser.parse(r);
			this.module = tree.getArtifactId();
			for (Node firstLevel : tree.getChildNodes()) {
				all.add(firstLevel.getGroupId());
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		directDependencies = all.stream().distinct().collect(Collectors.toList());
	}

	public List<String> getDirectDependencies() {
		return directDependencies;
	}

	public String getModule() {
		return module;
	}
}
