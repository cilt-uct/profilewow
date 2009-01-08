package org.sakaiproject.profilewow.tool.locators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.entitybroker.DeveloperHelperService;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.profilewow.tool.params.SakaiPersonViewParams;
import org.sakaiproject.profilewow.tool.producers.ViewProfileProducer;

import uk.ac.cam.caret.sakai.rsf.entitybroker.EntityViewParamsInferrer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ProfileViewParamatersInferer implements EntityViewParamsInferrer {

	private static Log log = LogFactory.getLog(SakaiPersonViewParams.class);
	

	
	public String[] getHandledPrefixes() {
		return new String[] {"profile"};
	}

	public ViewParameters inferDefaultViewParameters(String reference) {
		EntityReference ref = new EntityReference(reference);
		
		log.debug(reference.toString());
		return new SakaiPersonViewParams(ViewProfileProducer.VIEW_ID, ref.getId());
	}

}
