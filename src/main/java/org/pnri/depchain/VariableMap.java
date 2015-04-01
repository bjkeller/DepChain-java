package org.pnri.depchain;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class VariableMap {
	private Map<String, TreeSet<String>> varMap;
	
	public VariableMap() {
		varMap = new TreeMap<String, TreeSet<String>>();
	}
	
	public void put(String varName, String attrString) {
		TreeSet<String> attrSet;
		if (varMap.containsKey(varName)) {
			attrSet = varMap.get(varName);
		} else {
			attrSet = new TreeSet<>();
		}
		attrSet.add(attrString);
		varMap.put(varName, attrSet);
	}
	
	

}
