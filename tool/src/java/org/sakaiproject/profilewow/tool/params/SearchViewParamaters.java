package org.sakaiproject.profilewow.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class SearchViewParamaters extends SimpleViewParameters {
	
	public String searchText;
	public String start;
	
	public SearchViewParamaters() {
		
	}
	public SearchViewParamaters(String viewId) {
		this.viewID = viewId;
	}
	public SearchViewParamaters(String viewId, String text, String start) {
		this.viewID = viewId;
		this.start = start;
		this.searchText = text;
	}
}
