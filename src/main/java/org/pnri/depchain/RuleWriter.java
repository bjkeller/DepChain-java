package org.pnri.depchain;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import colibri.lib.ComparableSet;
import colibri.lib.Concept;
import colibri.lib.Lattice;

/**
 * RuleWriter - generate association rules from intervals constructed by
 * "edge" visits.
 * 
 * @author bjkeller
 *
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved.
 */
public class RuleWriter implements ConceptVisitor {
	
	private Writer out;
	private Lattice lattice;
	private TreeMap<Concept,ArrayList<LatticeInterval>> intervals;

	public RuleWriter(Writer outWriter, Lattice lat) {
		out = outWriter;
		lattice = lat;
		intervals = new TreeMap<>(new ConceptDepOrder());
	}

	@Override
	public void visit(Concept con) throws IOException {
		//do nothing
	}

	@Override
	public void visitEdge(Concept superConcept, Concept subConcept) throws IOException {
		//if not top
		if (!lattice.top().equals(superConcept) && !lattice.bottom().equals(subConcept)) {
			//find intervals for superConcept
			ArrayList<LatticeInterval> list = intervals.get(superConcept);
			//for each 
			if (list != null) {
				for(LatticeInterval interval : list) {
				//  extend by subconcept
					LatticeInterval ext = interval.extendMin(subConcept);
				//  if new interval has nontrivial rule then output
					//don't extend interval with nontrivial rule
					if (!checkRule(ext)) {
						addInterval(ext);
						//TODO would like to tell ConceptSearch not to bother
					}
				}
			} else {
				System.err.println("superconcept not found");
			}
		}
	}


	// Looks for a nontrivial rule for a particular interval.
	// Trivial rule for interval $[\mu(A\cup\{m\}),\mu(\{m\})]$ 
	// where $A$ is set of attributes and $m$ is an attribute,
	// looks like $A\cup\{m\}\rightarrow m$.
	// Non-trivial rule $A\rightarrow m$ exists when 
	// $\mu(A\cup\{m\}) = \mu(A)$
	// returns true if ext has nontrivial rule.
	// 
	@SuppressWarnings("rawtypes")
	private boolean checkRule(LatticeInterval ext) throws IOException {
		ComparableSet coatomAttributes = ext.getMax().getAttributes();
		ComparableSet minAttributes = ext.getMin().getAttributes();
		TreeSet<Comparable> diffAttributes = new TreeSet<>();
		for (Comparable element : minAttributes) {
			if (!coatomAttributes.contains(element)) {
				diffAttributes.add(element);
			}
		}
		Concept diffConcept = lattice.conceptFromAttributes(diffAttributes);
		long obstacleCount = violationSize(ext.getMin(),diffConcept);
		//if (obstacleCount == 0) {
		writeSet(coatomAttributes.iterator());
		out.write("\t");
		if (ext.getMin().equals(diffConcept)) {
			writeSet(diffAttributes.iterator());
			out.write("->");
			writeSet(coatomAttributes.iterator());
			out.write("\t"+obstacleCount+"\n");
			out.write("\n");
			return true;
		} else {
			//TODO remove me once know what's going on
			writeSet(ext.getMin().getAttributes().iterator());
			out.write("\t"+obstacleCount+"\n");
			return false;
		}
		
	}

	@SuppressWarnings("rawtypes")
	private long violationSize(Concept a_m, Concept a) {
		// TODO Auto-generated method stub
		ComparableSet a_mObjects = a_m.getObjects();
		ComparableSet aObjects = a.getObjects();
		TreeSet<Comparable> diffObjects = new TreeSet<>();
		for (Comparable element : aObjects) {
			if (!a_mObjects.contains(element)) {
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

	private void addInterval(LatticeInterval interval) {
		ArrayList<LatticeInterval> list = intervals.get(interval.getMin());
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(interval);
		intervals.put(interval.getMin(), list);
	}

	@Override
	public void before() throws IOException {
		//create intervals for coatoms
		Concept top = lattice.top();
		Iterator<Concept> coatomIterator = lattice.lowerNeighbors(top);
		while (coatomIterator.hasNext()) {
			Concept coatom = (Concept)coatomIterator.next();
			ArrayList<LatticeInterval> list = new ArrayList<>();
			list.add(new LatticeInterval(coatom, coatom));
			intervals.put(coatom,list);
		}
	}

	

	@Override
	public void after() throws IOException {
		// do nothing (?)

	}

}
