package de.jonas.output;

import java.util.List;

import de.jonas.analyzer.ProblematicDependency;

public interface Outputter {

	void output(List<ProblematicDependency> problems);

}
