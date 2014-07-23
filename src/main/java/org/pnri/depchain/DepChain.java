package org.pnri.depchain;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.Relation;
import colibri.lib.TreeRelation;

/**
 * Implements the dependency chain search [http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3392047/]
 * for data given either as pairs representing object-attribute incidence relation, or
 * as table where columns represent variables, rows represent subjects, and entries are binned values.
 * Dependency chains are computed from coatoms to atoms with no bound.
 * 
 * @author bjkeller
 * 
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved.
 * 
 */

public class DepChain {

	public static void main(String[] args) {
		DepArguments dargs = new DepArguments();
		JCommander cmd = new JCommander(dargs);
		try {
			cmd.parse(args);
			
			System.out.println("DepChain");
			System.out.println("\tinput: "+dargs.getInFile());
			System.out.println("\toutput: "+dargs.getOutFile());
			
			Path infile =  Paths.get(dargs.getInFile());
			Path outFile = Paths.get(dargs.getOutFile());
			
			Relation relation = new TreeRelation();
			if (dargs.getReadPairs()) {
				System.out.println("reading input in pair form");
				ObjectAttributeReader rdr = new ObjectAttributeReader();
				rdr.read(infile, relation);
			} else {
				ObservationReader rdr = new ObservationReader();
				rdr.read(infile, relation);
			}
			
			Lattice lattice = new HybridLattice(relation);

			InfoSearch searcher = new ConceptSearch(relation,lattice);

			searcher.search(outFile);
		}
		catch (ParameterException e) {
			System.out.println(e.getMessage());
			cmd.usage();

		} catch (IOException e) {
			System.out.println("error opening output file "+e.getMessage());
		} 
	}



}
