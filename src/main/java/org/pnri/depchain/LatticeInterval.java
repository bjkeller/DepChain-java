package org.pnri.depchain;

import colibri.lib.Concept;

/**
 * LatticeInterval class to represent intervals of concept lattice.
 * 
 * @author bjkeller
 *
 * Copyright (c) 2014 Benjamin J. Keller, all rights reserved.
 *
 */
public class LatticeInterval {
	
	private Concept min;
	private Concept max;
	private int obstacles;

	public LatticeInterval(Concept minConcept, Concept maxConcept) {
		min = minConcept;
		max = maxConcept;
		obstacles = 0;
	}

	public LatticeInterval extendMin(Concept newMinConcept) {
		return new LatticeInterval(newMinConcept, max);
	}
	
	public LatticeInterval extendMin(Concept newMinConcept, int obstacleCount) {
		LatticeInterval interval = new LatticeInterval(newMinConcept, max);
		interval.obstacles = obstacleCount;
		return interval;
	}

	//check that min <= con <= max
	public boolean contains(Concept con) {
		return subconcept(min,con) && subconcept(con,max);
	}
	
	//TODO should be elsewhere
	private boolean subconcept(Concept con1, Concept con2) {
		if (0 <= con2.getObjects().size() - con1.getObjects().size()) { 
			return con2.getObjects().containsAll(con1.getObjects());
		} else { //know con1 > con2 by size
			return false;
		}
	}

	public Concept getMin() { 
		return min;
	}

	public Concept getMax() {
		return max;
	}

	public int getViolationSize() {
		return obstacles;
	}

}
