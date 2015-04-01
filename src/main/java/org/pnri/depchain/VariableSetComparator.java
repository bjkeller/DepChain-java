package org.pnri.depchain;

import java.util.Comparator;

public class VariableSetComparator implements Comparator<VariableSet> {

	@Override
	public int compare(VariableSet set1, VariableSet set2) {
		int test = set1.size() - set2.size();
		// TODO Auto-generated method stub
		return test;
	}

}
