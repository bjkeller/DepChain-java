package io.ncidenz.depchain;

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
	
	@Parameter(names= { "-p", "--pairs" }, description="pair input flag")
	private boolean readPairs = false;

	@Parameter(names = { "-o", "--output_file" }, description="output file name", required = true)
	private String outFile;

	public String getInFile() { return inFile; }
	public String getOutFile() { return outFile; }
	public boolean getReadPairs() { return readPairs; }
		
}
