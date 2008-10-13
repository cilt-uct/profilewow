package org.sakaiproject.profilewow.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class SearchViewParamaters extends SimpleViewParameters {
	
	public String searchText;
	
	public SearchViewParamaters() {
		
	}
	public SearchViewParamaters(String viewId) {
		this.viewID = viewId;
	}
}
