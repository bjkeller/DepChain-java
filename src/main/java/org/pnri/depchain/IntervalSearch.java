package org.pnri.depchain;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import colibri.lib.ComparableSet;
import colibri.lib.Concept;
import colibri.lib.Lattice;
import colibri.lib.Relation;

/**
 * 
 * @author bjkeller
 *
 */

public class IntervalSearch implements InfoSearch {
	
	private Relation relation;
	private Lattice lattice;
	private IntervalSet intervals;
	private Writer out;
	
	public IntervalSearch(Relation rel, Lattice lat, Writer outWriter) {
		relation = rel;
		lattice = lat;
		intervals = new IntervalSet();
		out = outWriter;
	}

	@Override
	public void search(ConceptVisitor visitor) throws IOException {
		visitor.before();
		PriorityQueue<LatticeInterval> toVisit = new PriorityQueue<LatticeInterval>(relation.getSizeAttributes(),new IntervalDepOrder());
		Concept top = lattice.top();
		Iterator<Concept> coatomIterator = lattice.lowerNeighbors(top);
		while (coatomIterator.hasNext()) {
			Concept coatom = (Concept)coatomIterator.next();
			LatticeInterval interval = new LatticeInterval(coatom, coatom);
			toVisit.add(interval);
			intervals.add(interval);
			//visitor.visitEdge(top, coatom);
		}
		functionSearch(toVisit,visitor);
		visitor.after();
	}

	private void functionSearch(PriorityQueue<LatticeInterval> toVisit,
			ConceptVisitor visitor) throws IOException {
		
		Set<Concept> visitedConcepts = new TreeSet<Concept>(new ConceptDepOrder());
		
		while(!toVisit.isEmpty()) {
			LatticeInterval next = toVisit.remove();
			
			
			if (!visitedConcepts.contains(next.getMin())) {
				visitedConcepts.add(next.getMin());
				
				//visit?
				
				Iterator<Concept> subIterator = lattice.lowerNeighbors(next.getMin());
				if (subIterator.hasNext()) {
					Set<LatticeInterval> minSet = new TreeSet<LatticeInterval>(new IntervalViolOrder());

					Concept subConcept = subIterator.next();
					int obstCount = checkViolation(next,subConcept);
					LatticeInterval subInterval = next.extendMin(subConcept,obstCount);
					//
					writeSet(next.getMax().getAttributes().iterator());
					out.write("\t");
					writeSet(next.getMin().getAttributes().iterator());
					out.write("\t");
					writeSet(subConcept.getAttributes().iterator());
					out.write("\t"+obstCount+"\t"+subConcept.getObjects().size()+"\n");
					//
					int minViol = obstCount;
					minSet.add(subInterval);
					
					while (subIterator.hasNext()) {
						System.out.println("next "+minViol);
						subConcept = subIterator.next();
						obstCount = checkViolation(next,subConcept);
						subInterval = next.extendMin(subConcept,obstCount);
						//
						writeSet(next.getMax().getAttributes().iterator());
						out.write("\t");
						writeSet(next.getMin().getAttributes().iterator());
						out.write("\t");
						writeSet(subConcept.getAttributes().iterator());
						out.write("\t"+obstCount+"\t"+subConcept.getObjects().size()+"\n");
						//
						if (obstCount <= minViol) {
							if (obstCount < minViol) {
								minViol = obstCount;
								minSet = new TreeSet<LatticeInterval>(new IntervalViolOrder());
							}
							minSet.add(subInterval);
						}
					}
					if (minViol > 0) {
						for (LatticeInterval ext : minSet) {
							toVisit.add(ext);
							//visitor.visitEdge(next.getMin(),concept);
						}
					}
				}
			}
		}
		
		
	}

	@SuppressWarnings("rawtypes")
	private int checkViolation(LatticeInterval next, Concept subConcept) {
		ComparableSet coatomAttributes = next.getMax().getAttributes();
		TreeSet<Comparable> diffAttributes = new TreeSet<>();
		for (Comparable element : subConcept.getAttributes()) {
			if (!coatomAttributes.contains(element)) {
				diffAttributes.add(element);
			}
		}
		Concept diffConcept = lattice.conceptFromAttributes(diffAttributes);
		TreeSet<Comparable> diffObjects = new TreeSet<>();
		for (Comparable element : diffConcept.getObjects()) {
			if (!subConcept.getObjects().contains(element)) {
				diffObjects.add(element);
			}
		}
		return diffObjects.size();
	}

	@SuppressWarnings("rawtypes")
	private void writeSet(Iterator it) throws IOException {
		out.write("{");
		if (it.hasNext()) {
			String elem = (it.next()).toString();
			out.write(elem);
			while (it.hasNext()) {
				elem = (it.next()).toString();
				out.write(","+elem);
			}
		}
		out.write("}");

		
	}

}
