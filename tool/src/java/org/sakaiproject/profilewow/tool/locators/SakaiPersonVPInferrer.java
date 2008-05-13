package org.sakaiproject.profilewow.tool.locators;

import org.sakaiproject.profilewow.tool.producers.ViewProfileProducer;

import uk.ac.cam.caret.sakai.rsf.entitybroker.EntityViewParamsInferrer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class SakaiPersonVPInferrer implements EntityViewParamsInferrer {

	public String[] getHandledPrefixes() {
		return new String[] {"SakaiPerson"};
	}

	public ViewParameters inferDefaultViewParameters(String reference) {
		// TODO Auto-generated method stub
		return new SimpleViewParameters(ViewProfileProducer.VIEW_ID);
	}

}
