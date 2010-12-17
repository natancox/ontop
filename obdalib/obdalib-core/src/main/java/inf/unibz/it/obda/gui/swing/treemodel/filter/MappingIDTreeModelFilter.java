package inf.unibz.it.obda.gui.swing.treemodel.filter;

import inf.unibz.it.obda.domain.OBDAMappingAxiom;

public class MappingIDTreeModelFilter implements
		TreeModelFilter<OBDAMappingAxiom> {

	private final String srtIDTreeModelFilter;

	public MappingIDTreeModelFilter(String srtIDTreeModelFilter) {
		this.srtIDTreeModelFilter = srtIDTreeModelFilter;
	}

	@Override
	public boolean match(OBDAMappingAxiom object) {
		// TODO Auto-generated method stub
		boolean filterValue = false;
		OBDAMappingAxiom mapping = object;
		if (mapping.getId().indexOf(srtIDTreeModelFilter) != -1)
			filterValue = true;
		return filterValue;
	}

}
