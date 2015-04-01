package org.pnri.depchain;

import java.io.IOException;
import java.io.Writer;

import colibri.lib.Concept;

public class DotChainVisitor implements ChainVisitor {

	private DotWriter dw;
	

	public DotChainVisitor(Writer out) {
		dw = new DotWriter(out);
	}

	@Override
	public void visit(Concept c) throws IOException {
				dw.visit(c);
	}
	

	@Override
	public void visitEdge(Concept superConcept, Concept subConcept)
			throws IOException {
	
		dw.visit(superConcept);
		dw.write("->");
		dw.visit(subConcept);
		dw.write("\n");
	
	}

}
