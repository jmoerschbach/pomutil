package de.jonas.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.jonas.analyzer.ProblematicDependency;

public class FileOutputter implements Outputter {

	private File outputFile;

	public FileOutputter(File f) {
		this.outputFile = f;
	}

	@Override
	public void output(List<ProblematicDependency> problems) {
		try (BufferedWriter w = new BufferedWriter(new FileWriter(outputFile))) {
			for (ProblematicDependency p : problems) {
				w.write(p.getFileName() + ":");
				w.newLine();
				for (String i : p.getProblematicStatements()) {
					w.write(i);
					w.newLine();
				}
				w.newLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
