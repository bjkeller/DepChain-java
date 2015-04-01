package org.pnri.depchain;

import java.util.ArrayList;
import java.util.TreeMap;

import colibri.lib.Concept;

public class IntervalSet {
	private TreeMap<Concept,ArrayList<LatticeInterval>> intervals;
	
	public IntervalSet() {
		intervals = new TreeMap<>(new ConceptDepOrder());
	}
	
	void add(LatticeInterval interval) {
		ArrayList<LatticeInterval> list = intervals.get(interval.getMin());
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(interval);
		intervals.put(interval.getMin(), list);
	}
}
