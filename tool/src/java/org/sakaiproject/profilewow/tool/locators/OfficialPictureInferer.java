package org.sakaiproject.profilewow.tool.locators;

import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.profilewow.tool.params.ImageViewParamaters;
import org.sakaiproject.rsf.entitybroker.EntityViewParamsInferrer;

import lombok.extern.slf4j.Slf4j;
import uk.org.ponder.rsf.viewstate.ViewParameters;

@Slf4j
public class OfficialPictureInferer implements EntityViewParamsInferrer {

	public String[] getHandledPrefixes() {
		
		return new String[]{"official_picture"};
	}

	public ViewParameters inferDefaultViewParameters(String reference) {
		EntityReference ref = new EntityReference(reference);
		log.debug(reference.toString()); 
		return new ImageViewParamaters("imageServlet", ref.getId());
	}

}
