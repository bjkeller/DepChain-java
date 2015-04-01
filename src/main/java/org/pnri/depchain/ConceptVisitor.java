package org.pnri.depchain;

import java.io.IOException;

import colibri.lib.Concept;

/**
 * 
 * Visitor interface for colibri.lib.Concept objects
 * 
 * @author bjkeller
 *
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved.
 * 
 */
public interface ConceptVisitor {
	void visit(Concept con) throws IOException;

	void visitEdge(Concept next, Concept subConcept) throws IOException;

	void before() throws IOException;

	void after() throws IOException;
}
