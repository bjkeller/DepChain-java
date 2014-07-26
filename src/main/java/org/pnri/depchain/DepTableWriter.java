package org.pnri.depchain;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import colibri.lib.ComparableSet;
import colibri.lib.Concept;
import colibri.lib.Lattice;
import colibri.lib.Relation;

/**
 * Visitor to write concept to emphasize dependencies among attributes.
 * 
 * Since colibri-java doesn't use generics, have to suppress rawtype warnings.
 * 
 * @author bjkeller
 * 
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved.
 *
 */
public class DepTableWriter implements ConceptVisitor {
	
	private Writer out;
	private Relation relation;

	public DepTableWriter(Writer outWriter, Relation rel, Lattice lat) throws IOException {
		out = outWriter;
		relation = rel;
	}

	/*
	 * writes a row with number of attributes, lists of attributes and objects, and partial entropy
	 */
	@Override
	public void visit(Concept con) throws IOException {
		out.write(""+con.getAttributes().size());
		out.write("\t");
		writeSet(out,con.getAttributes());
		out.write("\t"+con.getObjects().size());
	
		out.write("\t"+entropy(objectProbability(con)));
		out.write("\t"+entropy(attributeProbability(con)));
		out.write("\t"+entropy(objectJaccard(con)));
		out.write("\t"+entropy(attributeJaccard(con)));
		out.write("\n");
	}

	@SuppressWarnings("rawtypes")
	private void writeSet(Writer out, ComparableSet set) throws IOException {
		Iterator<Comparable> setIterator = set.iterator();
		if (setIterator.hasNext()) {
			String elem = (setIterator.next()).toString();
			out.write(elem);
			while (setIterator.hasNext()) {
				elem = (setIterator.next()).toString();
				out.write(","+elem);
			}
			
		}
		
	}
	
	private double entropy(double prob) {
		double entropy = 0.0;
		if (0.0 < prob && prob < 1.0) {
			double log_prob = Math.log(prob)/Math.log(2);
			entropy = -1*(prob*log_prob);
		}
		return entropy;
	}

	private double objectProbability(Concept con) {
		double prob = 0.0;
		if (!con.getObjects().isEmpty()) {
			prob = con.getObjects().size() / (double)relation.getSizeObjects();
		}
		return prob;
	}

	private double attributeProbability(Concept con) {
		double prob = 0.0;
		if (!con.getAttributes().isEmpty()) {
			prob = con.getAttributes().size() / (double)relation.getSizeAttributes();
		}
		return prob;
	}
	
	@SuppressWarnings("rawtypes")
	private double objectJaccard(Concept con) {
		double result = 0.0;
		if (!con.getObjects().isEmpty()) {
			Set<Comparable> attrUnion = new TreeSet<>(); 
			Iterator<Comparable> attrIterator = con.getAttributes().iterator();
			while (attrIterator.hasNext()) {
				attrUnion.addAll(relation.getObjectSet(attrIterator.next()));
			}
			result = con.getObjects().size() / (double)attrUnion.size();
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private double attributeJaccard(Concept con) {
		double result = 0.0;
		if (!con.getAttributes().isEmpty()) {
			Set<Comparable> objUnion = new TreeSet<>();
			Iterator<Comparable> objIterator = con.getObjects().iterator();
			while (objIterator.hasNext()) {
				objUnion.addAll(relation.getAttributeSet(objIterator.next()));
			}
			result = con.getAttributes().size() / (double)objUnion.size();
		}
		return result;
	}

	@Override
	public void visitEdge(Concept next, Concept subConcept) {
		// Does nothing
		
	}

	@Override
	public void pre() throws IOException {
		// do nothing
		
	}

	@Override
	public void post() throws IOException {
		// do nothing
		
	}

}
