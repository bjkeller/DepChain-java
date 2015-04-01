package org.pnri.depchain;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.Test;

import colibri.lib.Concept;
import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.Relation;
import colibri.lib.TreeRelation;

public class LatticeIntervalTest {

	@Test
	public void test() {
		
		Relation relation = new TreeRelation();
		//    a1 a2 a3
		// o1  x  x  x
		// o2     x  x
		// o3        x
		relation.add("o1", "a1");
		relation.add("o1", "a2");
		relation.add("o1", "a3");
		relation.add("o2", "a2");
		relation.add("o2", "a3");
		relation.add("o3", "a3");
		Lattice lattice = new HybridLattice(relation);
		
		@SuppressWarnings("rawtypes")
		Collection<Comparable> oset1 = new TreeSet<Comparable>();
		oset1.add("o1");
		Concept c1 = lattice.conceptFromObjects(oset1);
		
		@SuppressWarnings("rawtypes")
		Collection<Comparable> oset2 = new TreeSet<Comparable>();
		oset2.add("o1");
		oset2.add("o2");
		Concept c2 = lattice.conceptFromObjects(oset2);
		
		@SuppressWarnings("rawtypes")
		Collection<Comparable> oset3 = new TreeSet<Comparable>();
		oset3.add("o1");
		oset3.add("o2");
		oset3.add("o3");
		Concept c3 = lattice.conceptFromObjects(oset3);
		
		LatticeInterval int1 = new LatticeInterval(c3,c3);
		// check c3 is in interval, c1 and c2 are out
		assertTrue(int1.contains(c3));
		assertFalse(int1.contains(c2));
		assertFalse(int1.contains(c1));
		
		LatticeInterval int2 = int1.extendMin(c2);
		// check c3 and c2 are both in int2
		assertTrue(int2.contains(c3));
		assertTrue(int2.contains(c2));
		assertFalse(int2.contains(c1));
		
		LatticeInterval int3 = int2.extendMin(c1);
		//check all three are in int3
		assertTrue(int3.contains(c3));
		assertTrue(int3.contains(c2));
		assertTrue(int3.contains(c1));

	}

}
