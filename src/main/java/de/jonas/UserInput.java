package de.jonas;

import com.beust.jcommander.Parameter;

import de.jonas.analyzer.Dummy;
import org.apache.commons.io.ByteOrderMark;

public class UserInput {
	@Parameter(names = { "--mavenDependency",
			"--md" }, description = "The maven dependency tree created via mvn dependency:tree")
	private String mavenDepencencyTree = "tree.txt";
	@Parameter(names = { "--root",
			"--r" }, description = "The root directory for searching all subpackages. Default NEEDS TO BE CHANGED", required = true)
	private String rootPackage = "src\\main\\java\\de";
	@Parameter(names = { "--output", "--o" }, description = "The directory/file the output is written to")
	private String outputFile = "output";

	public String getMavenDepencencyTree() {
		return mavenDepencencyTree;
	}

	public String getRootPackage() {
		return rootPackage;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void bla() {
		Dummy bla = new Dummy();
		ByteOrderMark blub = new ByteOrderMark(null, null);
	}
}
