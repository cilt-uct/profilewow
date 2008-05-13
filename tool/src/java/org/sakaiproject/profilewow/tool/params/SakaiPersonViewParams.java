package org.sakaiproject.profilewow.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class SakaiPersonViewParams extends SimpleViewParameters {
	
	public String id;
	
	
	public SakaiPersonViewParams() {
		super();
	}
	
	public SakaiPersonViewParams(String pId) {
		super();
		id = pId;
	}

	public SakaiPersonViewParams(String viewID, String pId) {
		
		this.viewID = viewID;
		id = pId;
	}
	
}
