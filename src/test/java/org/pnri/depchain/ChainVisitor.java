package org.pnri.depchain;

import java.io.IOException;

import colibri.lib.Concept;

public interface ChainVisitor {
	void visit(Concept c) throws IOException;
	void visitEdge(Concept superConcept, Concept subConcept) throws IOException;
}
