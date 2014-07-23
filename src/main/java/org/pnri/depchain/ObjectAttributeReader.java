package org.pnri.depchain;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import colibri.lib.Relation;

/**
 * Read incidence relation as object-attribute pairs from ASCII text file with two 
 * whitespace separated columns.
 *  
 * @author bjkeller
 *
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved.
 */
public class ObjectAttributeReader {
	
	public void read(Path infile, Relation relation) {
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader rdr = Files.newBufferedReader(infile, charset)){
			String line = null;
			while ((line = rdr.readLine()) != null) {
				String[] toks = line.split("\\s");
				if (toks.length == 2) {
					relation.add(toks[0], toks[1]);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading object-attribute file: " + e.getMessage());
		}
	}

}
