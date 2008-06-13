package org.sakaiproject.profilewow.tool.producers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.profilewow.tool.params.ImageViewParamaters;
import org.sakaiproject.profilewow.tool.params.SakaiPersonViewParams;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class ViewProfileProducer implements ViewComponentProducer,ViewParamsReporter {

	private static final String NO_PIC_URL = "../images/noimage.gif";
	private static Log log = LogFactory.getLog(ViewProfileProducer.class);
	
	public final static String VIEW_ID="viewProfile";
	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}

	private SakaiPersonManager sakaiPersonManager;
	public void setSakaiPersonManager(SakaiPersonManager spm) {
		sakaiPersonManager = spm;
	}
	
	private ViewParameters viewparams;
	public void setViewparams(ViewParameters viewparams) {
		this.viewparams = viewparams;
	}
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		// TODO Auto-generated method stub
		
		SakaiPersonViewParams svp = (SakaiPersonViewParams) viewparams;
		SakaiPerson sPerson = null;
		if (svp.id != null) {
			try{
				String userId = userDirectoryService.getUserId(svp.id);
				//and get the SakaiPerson here
				sPerson = sakaiPersonManager.getSakaiPerson(userId, sakaiPersonManager.getUserMutableType());

			} catch (UserNotDefinedException e) {
				e.printStackTrace();
			}

			if (sPerson == null) {
				log.error("No sakaiperson with id: " + svp.id);
			}
			log.debug("got profile for: " + sPerson.getGivenName() + " " + sPerson.getSurname() + " with id " + sPerson.getId());
		} else {
			log.debug("getting profile for current user");
			sPerson = sakaiPersonManager.getSakaiPerson(sakaiPersonManager.getUserMutableType());
		}
		
		//picture stuff
		String picUrl = sPerson.getPictureUrl();
		if (picUrl == null || picUrl.trim().length() == 0)
			picUrl = NO_PIC_URL;
		else 
			picUrl = sPerson.getPictureUrl();
	
		if (sPerson.isSystemPicturePreferred() != null &&  sPerson.isSystemPicturePreferred().booleanValue()) {
			UIInternalLink.make(tofill, "photo", new ImageViewParamaters("imageServlet", sPerson.getUuid()));
		} else if (sPerson.isSystemPicturePreferred() == null || !sPerson.isSystemPicturePreferred().booleanValue() ) {
			UILink.make(tofill, "photo", picUrl);
		} 
		
		
		String fullName = "";
		
		//if (sPerson.getTitle() != null)
			//fullName =  sPerson.getTitle() + " ";
		
		if (sPerson.getGivenName() != null)
			fullName += sPerson.getGivenName()+ " ";
		
		if (sPerson.getSurname() != null)
			fullName += sPerson.getSurname();
		
		
		UIOutput.make(tofill, "full-name", fullName);
		
		UIOutput.make(tofill, "header-name", fullName);
		
		Boolean hidePInfo = false;
		if (sPerson.getHidePrivateInfo() == null || sPerson.getHidePrivateInfo())
			hidePInfo = true;
		
		UILink.make(tofill,"email", sPerson.getMail(),"mailto:" + sPerson.getMail());
		
		if (sPerson.getOrganizationalUnit() != null)
			UIOutput.make(tofill,"org", sPerson.getOrganizationalUnit());
		
		if (sPerson.getLocalityName() != null) 
			UIOutput.make(tofill,"country", sPerson.getLocalityName());
		if (sPerson.getLabeledURI() != null)
			UILink.make(tofill,"homepage", sPerson.getLabeledURI(), sPerson.getLabeledURI());
		
		if (sPerson.getTelephoneNumber() != null && !hidePInfo )
			UIOutput.make(tofill,"workphone", sPerson.getTelephoneNumber());
		
		if (sPerson.getMobile() != null && !hidePInfo)
			UIOutput.make(tofill,"mobile", sPerson.getMobile());
		
		if (sPerson.getNotes() != null)
				UIOutput.make(tofill,"more-info", sPerson.getNotes());
		
		
		

	}
	

	public ViewParameters getViewParameters() {
		// TODO Auto-generated method stub
		return new SakaiPersonViewParams();
	}


}
