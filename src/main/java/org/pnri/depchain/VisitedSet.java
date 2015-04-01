package org.pnri.depchain;

import java.util.TreeMap;
import java.util.TreeSet;

//forgetful visited set --- graded search means can throw away smaller sets
public class VisitedSet {
	TreeMap<Integer, TreeSet<VariableSet>> visitMap;
	int minSize;
	
	public VisitedSet() {
		visitMap = new TreeMap<>();
		minSize = 0;
	}

	//TODO deal with unexpected smaller set
	public boolean contains(VariableSet set) {
		if (set.size() >= minSize) {
			TreeSet<VariableSet> visitedSets = visitMap.get(set.size());
			return visitedSets != null && visitedSets.contains(set);
		}
		return false;
	}

	public void add(VariableSet set) {
//TODO when do we cleanup?
		TreeSet<VariableSet> visitedSets = visitMap.get(set.size());
		if (visitedSets == null) {
			visitedSets = new TreeSet<>();
		}
		visitedSets.add(set);
		visitMap.put(set.size(), visitedSets);
	}
	
	public void cleanup(int size) {
		while (minSize < size) { //clean up!
			visitMap.remove(minSize);
			minSize++;
		}
	}

}
