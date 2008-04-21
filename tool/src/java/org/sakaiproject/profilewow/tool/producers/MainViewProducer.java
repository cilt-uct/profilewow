package org.sakaiproject.profilewow.tool.producers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.user.api.UserDirectoryService;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class MainViewProducer implements DefaultView, ViewComponentProducer {

	public static final String VIEW_ID = "main"; 
	public String getViewID() {
		
		return VIEW_ID;
	}
	private static Log log = LogFactory.getLog(MainViewProducer.class);
	
	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}
	

	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		// TODO Auto-generated method stub

		UIInternalLink.make(tofill, "editProfile", new SimpleViewParameters(EditProducer.VIEW_ID));
		
		
		SakaiPerson sPerson = spm.getSakaiPerson(spm.getUserMutableType()); 
		
		if (sPerson != null ) {
			log.info("got profile for: " + sPerson.getGivenName() + " " + sPerson.getSurname());
			UIOutput.make(tofill,"profile-name", sPerson.getGivenName() + " " + sPerson.getSurname());
			// - not shown in main UIOutput.make(tofill,"profile-nickname", sPerson.getNickname());
			UIOutput.make(tofill,"profile-postion", sPerson.getTitle());
			UIOutput.make(tofill,"profile-department", sPerson.getOrganizationalUnit());
			UIOutput.make(tofill,"profile-school", sPerson.getCampus());
			UIOutput.make(tofill,"profile-room", sPerson.getRoomNumber());
			UIOutput.make(tofill,"profile-workphone", sPerson.getTelephoneNumber());
		}
			
		
	}

}
