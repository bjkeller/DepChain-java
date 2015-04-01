package org.pnri.depchain;

import com.beust.jcommander.Parameter;

/**
 * Arguments for Depchain using JCommander. 
 * Default input is as unlabeled table, but may also be object-attribute pairs if the pair flag
 * is given.
 *  
 * @author bjkeller
 * 
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved.
 *
 */

public class DepArguments {
	@Parameter(names= { "-i", "--input_file" }, description="input file name", required = true)
	private String inFile;
	
	@Parameter(names = { "-o", "--output_file" }, description="output file name", required = true)
	private String outFile;
	
	@Parameter(names= { "-p", "--pairs" }, description="pair input flag")
	private boolean readPairs = false;

	@Parameter(names= { "-d", "--dot" }, description="dot output flag")
	private boolean dotOutput = false;
	
	@Parameter(names = { "-r", "--rules" }, description="rule output flag")
	private boolean ruleOutput = false;

	public String getInFile() { return inFile; }
	public String getOutFile() { return outFile; }
	public boolean getReadPairs() { return readPairs; }
	public boolean getDotOutput() { return dotOutput; }
	public boolean getRuleOutput() { return ruleOutput; }
		
}
