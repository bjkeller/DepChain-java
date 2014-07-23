package org.pnri.depchain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import colibri.lib.Concept;
import colibri.lib.ConceptComparator;
import colibri.lib.ConceptOrder;
import colibri.lib.Lattice;
import colibri.lib.Relation;

/**
 * Implements dependency chain search based on [http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3392047/]
 * (contrasts with a chain search over the information lattice based on sets of variables for info theory
 * computations.)
 * 
 * NOTE: Based on colibri-java, but had to change accessibility of colibri.lib.ConceptComparator 
 * and colibri.lib.ConceptOrder to make this work.
 * 
 * @author bjkeller
 *
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved.
 * 
 */

public class ConceptSearch implements InfoSearch {

	private Relation relation;
	private Lattice lattice;

	public ConceptSearch(Relation rel, Lattice lat) {
		relation = rel;
		lattice = lat;
	}

	@Override
	public void search(Path outFile)  {
		System.out.println("attributes: "+relation.getSizeAttributes());
		System.out.println("objects: "+relation.getSizeObjects());
		try (BufferedWriter out = new BufferedWriter(new FileWriter(outFile.toFile()))) {
			ConceptVisitor visitor = new DepTableWriter(out,relation,lattice);
			Concept top = lattice.top();
			Iterator<Concept> coatomIterator = lattice.lowerNeighbors(top);
			PriorityQueue<Concept> toVisit = new PriorityQueue<Concept>(relation.getSizeAttributes(),new ConceptComparator(ConceptOrder.ATTR_SIZEFIRST));
			while (coatomIterator.hasNext()) {
				Concept concept = (Concept) coatomIterator.next();
				toVisit.add(concept);
			}
			searchChains(lattice, toVisit, visitor);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//from each coatom, follow chain formed by max cardinality subconcept(s) (may be ties).
	//for each concept, find other instantiations of variable set, and dump entropies
	private void searchChains(Lattice lattice, PriorityQueue<Concept> toVisit, ConceptVisitor visitor) throws IOException {
		//TODO consider what Set implementation to use for visited and maxSet
		//TODO  visitedConcepts should forget concepts wont see again 
		//TODO how to avoid attribute combinations that are low entropy

		Set<Concept> visitedConcepts = new TreeSet<Concept>(new ConceptComparator(ConceptOrder.ATTR_SIZEFIRST));

		while (!toVisit.isEmpty()) {
			Concept next = toVisit.remove();

			if (!visitedConcepts.contains(next)) {

				visitedConcepts.add(next);

				visitor.visit(next);

				Iterator<Concept> subIterator = lattice.lowerNeighbors(next);
				if (subIterator.hasNext()) {

					Concept maxConcept = subIterator.next();
					Set<Concept> maxSet = new TreeSet<Concept>(new ConceptComparator(ConceptOrder.ATTR_SIZEFIRST));

					maxSet.add(maxConcept);
					int maxSize = maxConcept.getObjects().size();
					while(subIterator.hasNext()) {
						Concept subConcept = subIterator.next();
						if (subConcept.getObjects().size() >= maxSize) {
							if (subConcept.getObjects().size() > maxSize) {
								maxSize = subConcept.getObjects().size();
								maxSet = new TreeSet<Concept>(new ConceptComparator(ConceptOrder.ATTR_SIZEFIRST));
							}
							maxSet.add(subConcept);
						}
					}
					for (Concept concept : maxSet) {
						toVisit.add(concept);
					}
				}
			}
		}

	}

}
