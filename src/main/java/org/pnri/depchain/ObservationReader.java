package org.pnri.depchain;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import colibri.lib.Relation;

/**
 * Read incidence relation from observation table given as ASCII test file.
 * Each column represents a variable and each row a subject/sample, with each entry
 * representing a binned value. Columns and rows are assumed to be unlabeled.
 * Objects are created from each row and given name "sr" where r is row number (zero-based)
 * Attributes are created from each column and value, given name "ac_v" where c is column number,
 * and v is value.
 * 
 * @author bjkeller
 * 
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved.
 *
 */
public class ObservationReader {

	public VariableMap read(Path infile, Relation relation) {
		
		VariableMap varMap = new VariableMap();
	
		String objNameString = "s";
		String attrNameString = "a";
		
		Charset charset = Charset.forName("US-ASCII");
		try(BufferedReader rdr = Files.newBufferedReader(infile, charset)) {
			
			int subj = 0;
			
			String line = null;
		    while ((line = rdr.readLine()) != null) {
		    	String[] toks = line.split("\\s");
		    	String objString = makeObject(objNameString,subj);
		    	
		    	int col = 0;
		    	while (col < toks.length) {
		    		String varString = makeVar(attrNameString, col);
		    		String attrString = makeAttr(varString,toks[col]);
		    		relation.add(objString, attrString);
		    		varMap.put(varString, attrString);
		    		col++;
		    	}
		    	
		    	subj++;
		    }
		} catch (IOException e) {
			System.out.println("Error opening observation file: "+e.getMessage());
		}
		
		return varMap;
	}

	private String makeVar(String attrNameString, int col) {
		return attrNameString + col;
	}
	
	private String makeAttr(String varString, String val) {
		return varString + "_" + val;
	}

	private String makeObject(String objNameString, int subj) {
		return objNameString + subj;
	}

}
