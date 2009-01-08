package org.sakaiproject.profilewow.tool.locators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.profilewow.tool.params.ImageViewParamaters;

import uk.ac.cam.caret.sakai.rsf.entitybroker.EntityViewParamsInferrer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class OfficialPictureInferer implements EntityViewParamsInferrer {
	private static Log log = LogFactory.getLog(OfficialPictureInferer.class);
	public String[] getHandledPrefixes() {
		
		return new String[]{"official_picture"};
	}

	public ViewParameters inferDefaultViewParameters(String reference) {
		EntityReference ref = new EntityReference(reference);
		log.debug(reference.toString()); 
		return new ImageViewParamaters("imageServlet", ref.getId());
	}

}
