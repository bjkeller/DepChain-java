package org.pnri.depchain;

import java.util.Comparator;

public class IntervalDepOrder implements Comparator<LatticeInterval> {

	@Override
	public int compare(LatticeInterval o1, LatticeInterval o2) {
		int c = o1.getMin().getAttributes().size() - o2.getMin().getAttributes().size();
		if (c == 0) {
			c = -1*(o1.getMin().getObjects().size() - o2.getMin().getObjects().size());
			if (c == 0 ) {
				c = o1.getMin().getAttributes().compareTo(o2.getMin().getAttributes());
			}
		}
		return c;
	}

}
