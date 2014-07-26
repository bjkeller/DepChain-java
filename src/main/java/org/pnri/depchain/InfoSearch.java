package org.pnri.depchain;

import java.io.IOException;

/**
 * Common interface for (information) dependency chain search classes.
 * Expected to product output to outFile, but no constraints on lattice.
 * 
 * @author bjkeller
 * 
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved.
 */
public interface InfoSearch {
	public void search(ConceptVisitor visitor) throws IOException;
}
