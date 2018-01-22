package org.sakaiproject.profilewow.tool.locators;

import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.profilewow.tool.params.SakaiPersonViewParams;
import org.sakaiproject.profilewow.tool.producers.ViewProfileProducer;
import org.sakaiproject.rsf.entitybroker.EntityViewParamsInferrer;

import lombok.extern.slf4j.Slf4j;
import uk.org.ponder.rsf.viewstate.ViewParameters;

@Slf4j
public class ProfileViewParamatersInferer implements EntityViewParamsInferrer {

	

	
	public String[] getHandledPrefixes() {
		return new String[] {"profile"};
	}

	public ViewParameters inferDefaultViewParameters(String reference) {
		EntityReference ref = new EntityReference(reference);
		
		log.debug(reference.toString());
		return new SakaiPersonViewParams(ViewProfileProducer.VIEW_ID, ref.getId());
	}

}
