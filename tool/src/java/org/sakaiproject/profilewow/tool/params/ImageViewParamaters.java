package org.sakaiproject.profilewow.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class ImageViewParamaters extends SimpleViewParameters {

	public String userId;
	
	public ImageViewParamaters () {
		
	}
	
	public ImageViewParamaters(String userId) {
		
		this.userId = userId;
	
}
	
	public ImageViewParamaters(String viewId,String userId) {
	
			this.viewID = viewId;
			this.userId = userId;
		
	}
}
