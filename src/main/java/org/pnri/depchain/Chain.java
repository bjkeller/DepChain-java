package org.pnri.depchain;

import java.io.IOException;
import java.util.LinkedList;

import colibri.lib.Concept;

/**
 * Chain
 * Implements a formal concept lattice chain as a sequence of Concept objects.
 * Uses visitor pattern to access elements of chain.
 * 
 * @author bjkeller
 *
 */

public class Chain {
	public Chain() {
		list = new LinkedList<Concept>();
	}

	public long size() {
		return list.size();
	}

	public void add(Concept concept) {
		list.add(concept);
		
	}
	
	private LinkedList<Concept> list;

	public void accept(ChainVisitor cv) throws IOException {
		if (list.size() == 1) {
			cv.visit(list.getFirst());
		} else {
			Concept first = null;
			for (Concept con:list) {
				if (first != null) {
					cv.visitEdge(first, con);
				}
				first = con;
			}
		}
		
	}
}
