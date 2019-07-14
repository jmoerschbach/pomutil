package de.jonas.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ImportParser {

	private File toParse;
	private List<String> importStatements;

	public ImportParser(String file) {
		this(new File(file));
	}

	public ImportParser(File file) {
		this.toParse = file;
		parse();
	}

	private void parse() {
		List<String> imports = new ArrayList<>();
		try (Scanner input = new Scanner(toParse)) {
			while (input.hasNextLine()) {
				String currentLine = input.nextLine().trim();
				if (currentLine.startsWith("import ")) //TODO proper regex!
					imports.add(currentLine);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		importStatements = imports.stream().map(s -> s.replaceAll("import ", ""))
				.map(s -> s.replaceAll("static", "").trim()).collect(Collectors.toList());
	}

	public List<String> getAllImportStatements() {
		return importStatements;
	}
	
	public List<String> getNonJavaImportStatements() {
		return importStatements.stream().filter(s -> !s.startsWith("java.")).collect(Collectors.toList());
	}

}
