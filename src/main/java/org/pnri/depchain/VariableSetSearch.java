package org.pnri.depchain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import colibri.lib.Concept;
import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.Relation;
import colibri.lib.TreeRelation;

public class VariableSetSearch implements InfoSearch {

	private Relation relation;
	private Lattice lattice;
	private VariableMap varMap;

	public VariableSetSearch(Path infile) {
		relation = new TreeRelation();
		ObservationReader rdr = new ObservationReader();
		varMap = rdr.read(infile, relation);
		lattice = new HybridLattice(relation);
	}

	/*
	@Override
	public void search(Path outFile) throws IOException {
		Writer out = new BufferedWriter(new FileWriter(outFile.toFile()));
		Concept top = lattice.top();
		Iterator<Concept> coatomIterator = lattice.lowerNeighbors(top);
		PriorityQueue<VariableSet> toVisit = new PriorityQueue<VariableSet>();
		while (coatomIterator.hasNext()) {
			Concept concept = (Concept) coatomIterator.next();
			toVisit.add(new VariableSet(concept));
		}

		double searchBound = Math.exp(-1);

		VisitedSet visited = new VisitedSet();

		while (!toVisit.isEmpty()) {
			VariableSet next = toVisit.remove();

			if(!visited.contains(next)) {
				visited.add(next);

				writeSummary(out,next);

				Iterator<Concept> subIterator = lattice.lowerNeighbors(next.getConcept());
				if (subIterator.hasNext()) {
					Concept maxConcept = subIterator.next();
					Set<Concept> maxSet = new TreeSet<Concept>();

					if (jaccard(maxConcept) > searchBound) {
						maxSet.add(maxConcept);
						int maxSize = maxConcept.getObjects().size();
						while(subIterator.hasNext()) {
							Concept subConcept = subIterator.next();
							if (subConcept.getObjects().size() >= maxSize) {
								maxSize = subConcept.getObjects().size();
								maxSet.add(subConcept);
							}
						}
						for (Concept concept : maxSet) {
							VariableSet cSet = new VariableSet(concept);
							toVisit.add(cSet);
							if (cSet.size() > next.size() + 1) {
								//TODO deal with skipped size
								//want that have variable sets for each
							}
						}
					}

				}
			}

		}
	}
*/
	@SuppressWarnings("rawtypes")
	private double jaccard(Concept con) {
		double result = 0.0;
		if (!con.getObjects().isEmpty()) {
			Set<Comparable> attrUnion = new TreeSet<>(); 
			Iterator<Comparable> attrIterator = con.getAttributes().iterator();
			while (attrIterator.hasNext()) {
				attrUnion.addAll(relation.getObjectSet(attrIterator.next()));
			}
			result = con.getObjects().size() / (double)attrUnion.size();
		}
		return result;
	}

	private void writeSummary(Writer out, VariableSet next) throws IOException {
		out.write(next.size() + "\t");
		writeSet(out,next.getVariables());

		double sum = 0.0;
		double min = 1.0;
		double max = 0.0;
		int n = 0;

		//TODO iterate through instances of set

	}



	private void writeSet(Writer out, Set<String> variables) throws IOException {
		Iterator<String> varIterator = variables.iterator();
		if (varIterator.hasNext()) {
			String varString = varIterator.next();
			out.write(varString);
			while (varIterator.hasNext()) {
				varString = varIterator.next();
				out.write("-"+varString);
			}
		}

	}

	private double objectEntropy(Concept con) {
		double entropy = 0.0;
		if (!con.getObjects().isEmpty() && con.getObjects().size() != relation.getSizeObjects()) {
			double p_con = con.getObjects().size() / (double)relation.getSizeObjects();
			double log_p_con = Math.log(p_con)/Math.log(2);
			entropy = -1*(p_con*log_p_con);
		}
		return entropy;
	}

	@Override
	public void search(ConceptVisitor visitor) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
