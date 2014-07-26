package org.pnri.depchain;

import java.util.Comparator;

import colibri.lib.Concept;

/**
 * Better order for exploring dependencies: 
 * compare by 
 * - attr size, then 
 * - decreasing object size, then 
 * - attributes lexicographically
 * 
 * @author bjkeller
 *
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved
 */
public class ConceptDepOrder implements Comparator<Concept> {

	@Override
	public int compare(Concept c1, Concept c2) {
		int c = c1.getAttributes().size() - c2.getAttributes().size();
		if (c == 0) {
			c = -1*(c1.getObjects().size() - c2.getObjects().size());
			if (c == 0 ) {
				c = c1.getAttributes().compareTo(c2.getAttributes());
			}
		}
		return c;
	}

}
