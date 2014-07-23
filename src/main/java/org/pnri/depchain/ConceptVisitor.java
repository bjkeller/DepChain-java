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
}
