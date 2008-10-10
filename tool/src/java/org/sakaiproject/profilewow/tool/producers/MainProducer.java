package org.sakaiproject.profilewow.tool.producers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.profilewow.tool.params.ImageViewParamaters;
import org.sakaiproject.user.api.UserDirectoryService;

import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBoundString;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class MainProducer implements ViewComponentProducer, DefaultView {

	private static Log log = LogFactory.getLog(MainProducer.class);
	public static final String NO_PIC_URL = "../images/noimage.gif";
	
	public static String VIEW_ID = "main";
	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}

	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}
	
	private TargettedMessageList tml;
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.tml = tml;
	}
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		SakaiPerson sPerson = spm.getSakaiPerson(spm.getUserMutableType());
		if (sPerson == null) {
			log.debug("creating a new profile!");
			sPerson = spm.create(userDirectoryService.getCurrentUser().getId(), spm.getUserMutableType());
			spm.save(sPerson);
		}

		//picture stuff
		String picUrl = sPerson.getPictureUrl();
		if (picUrl == null || picUrl.trim().length() == 0)
			picUrl = NO_PIC_URL;
		else 
			picUrl = sPerson.getPictureUrl();

		
		if (sPerson.isSystemPicturePreferred() != null &&  sPerson.isSystemPicturePreferred().booleanValue()) {
			UIInternalLink.make(tofill, "current-pic", new ImageViewParamaters("imageServlet", sPerson.getUuid()));
		} else if (sPerson.isSystemPicturePreferred() == null || !sPerson.isSystemPicturePreferred().booleanValue() ) {
			UILink.make(tofill, "current-pic", picUrl);
		} 
		//edit link
		UIInternalLink.make(tofill, "editProfileLink",  UIMessage.make("editProfileLink"), new SimpleViewParameters(EditProducer.VIEW_ID));
		
		//The links for the upload
		UIInternalLink.make(tofill,"upload-link", new SimpleViewParameters(UploadPicture.VIEW_ID));
		UIInternalLink.make(tofill,"select-pic", new SimpleViewParameters(ChangePicture.VIEW_ID));
		
		log.debug("got profile for: " + sPerson.getGivenName() + " " + sPerson.getSurname());
		log.debug("uuid: " + sPerson.getUid() + ", agent_uuid: " + sPerson.getAgentUuid());
		
		UIOutput.make(tofill,"full-name", sPerson.getGivenName() + " " + sPerson.getSurname());
		UIOutput.make(tofill,"email", sPerson.getMail());
		// not sure what this is meant to be UIOutput.make(tofill,"position", sPerson.getPosition());
		UIOutput.make(tofill,"department", sPerson.getOrganizationalUnit());
		UIOutput.make(tofill,"school", sPerson.getCampus());
		UIOutput.make(tofill,"room", sPerson.getRoomNumber());
		UIOutput.make(tofill,"workphone", sPerson.getTelephoneNumber());
		UIOutput.make(tofill,"mobile", sPerson.getMobile());
		UIOutput.make(tofill,"moreinfo", sPerson.getNotes());
		UILink.make(tofill, "homepage", sPerson.getLabeledURI(),sPerson.getLabeledURI());

	}

}
