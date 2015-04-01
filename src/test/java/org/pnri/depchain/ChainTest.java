package org.pnri.depchain;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import colibri.lib.Concept;
import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.Relation;
import colibri.lib.TreeRelation;

public class ChainTest {
	
	private Lattice lattice;
	private Relation relation;
	
	@Before
	public void setup() {
		relation = new TreeRelation();
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
		lattice = new HybridLattice(relation);
	}

	@Test
	public void testEmpty() {
		Chain empty = new Chain();
		assertEquals(0, empty.size());
	}
	
	
	@Test
	public void testSingleton() {
		Chain chain = new Chain();
		
		@SuppressWarnings("rawtypes")
		Collection<Comparable> o1set = new TreeSet<Comparable>();
		o1set.add("o1");
		Concept concept = lattice.conceptFromObjects(o1set);
		
		chain.add(concept);
		
		assertEquals(1, chain.size());
		//TODO check that chain has correct contents using ChainVisitor
		StringWriter out = new StringWriter();
		ChainVisitor cv = new DotChainVisitor(out);
		try {
			chain.accept(cv);
			String expected = "\"o1;a1,a2,a3\"";
			assertEquals(expected, out.toString());
		} catch (IOException e) {
			fail("chain visitor exception "+ e);
		}
		
		
	}
	
	@Test
	public void testMultiple() {
		
		@SuppressWarnings("rawtypes")
		Collection<Comparable> oset1 = new TreeSet<Comparable>();
		oset1.add("o1");
		
		@SuppressWarnings("rawtypes")
		Collection<Comparable> oset2 = new TreeSet<Comparable>();
		oset2.add("o1");
		oset2.add("o2");
		
		@SuppressWarnings("rawtypes")
		Collection<Comparable> oset3 = new TreeSet<Comparable>();
		oset3.add("o1");
		oset3.add("o2");
		oset3.add("o3");
		
		Concept c1 = lattice.conceptFromObjects(oset1);
		Concept c2 = lattice.conceptFromObjects(oset2);
		Concept c3 = lattice.conceptFromObjects(oset3);
		
		Chain chain = new Chain();
		chain.add(c1);
		chain.add(c2);
		chain.add(c3);
		
		assertEquals(3, chain.size());
		StringWriter out = new StringWriter();
		ChainVisitor cv = new DotChainVisitor(out);
		try {
			chain.accept(cv);
			String expected = "\"o1;a1,a2,a3\"->\"o1,o2;a2,a3\"\n\"o1,o2;a2,a3\"->\"o1,o2,o3;a3\"\n";
			assertEquals(expected, out.toString() );
		} catch (IOException e) {
			fail("chain visitor exception "+ e);
		}
		
	}

	
}
