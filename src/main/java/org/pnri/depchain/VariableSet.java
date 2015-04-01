package org.pnri.depchain;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import colibri.lib.Concept;

public class VariableSet {

	private Set<String> variables;
	private Concept primaryConcept;

	@SuppressWarnings("rawtypes")
	public VariableSet(Concept concept) {
		primaryConcept = concept;
		variables = new TreeSet<>();
		for (Comparable attrString : concept.getAttributes()) {
			variables.add(varName(attrString.toString()));
		}
	}

	private String varName(String attrString) {
		int pos = attrString.indexOf('_');
		return attrString.substring(0, pos);
	}

	public int size() {
		return variables.size();
	}
	
	public int compareTo(VariableSet set) {
		int test = size() - set.size();
		if (test == 0) {
			Iterator<String> varIterator1 = variables.iterator();
			Iterator<String> varIterator2 = set.variables.iterator();
			while (varIterator1.hasNext() && test == 0) {
				String var1 = varIterator1.next();
				String var2 = varIterator2.next();
				test = var1.compareTo(var2);
			}
		}
		return test;
	}

	public Concept getConcept() {
		return primaryConcept;
	}

	public Set<String> getVariables() {
		return variables;
	}

}
