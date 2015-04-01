package org.pnri.depchain;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import colibri.lib.ComparableSet;
import colibri.lib.Concept;

/**
 * Implementation of visitor to print concept chains in dot format.
 * Abuse of the idea of a concept-visitor.
 * 
 * @author bjkeller
 *
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved
 */
public class DotWriter implements ConceptVisitor {

	private Writer out;

	public DotWriter(Writer outWriter) {
		out = outWriter;
	}
	
	@Override
	public void visit(Concept con) throws IOException {
		writeConcept(con);
	}
	
	@Override
	public void visitEdge(Concept superConcept, Concept subConcept) throws IOException {
		writeConcept(superConcept);
		out.write("->");
		writeConcept(subConcept);
		out.write("\n");
	}

	//TODO restore ability to write card of object set instead of set
	private void writeConcept(Concept con) throws IOException {
		out.write("\"");
		//out.write(""+con.getObjects().size());
		writeSet(out, con.getObjects());
		out.write(";");
		writeSet(out,con.getAttributes());
		out.write("\"");	
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

	@Override
	public void before() throws IOException {
		out.write("digraph lattice {\n");
		
	}

	@Override
	public void after() throws IOException {
		out.write("}\n");
		
	}

	public void write(String str) throws IOException {
		out.write(str);
		
	}

	

}
