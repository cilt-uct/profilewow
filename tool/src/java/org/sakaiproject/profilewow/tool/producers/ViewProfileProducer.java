package org.sakaiproject.profilewow.tool.producers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.profilewow.tool.params.ImageViewParamaters;
import org.sakaiproject.profilewow.tool.params.SakaiPersonViewParams;
import org.sakaiproject.profilewow.tool.producers.templates.ProfilePicRenderer;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.util.FormattedText;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class ViewProfileProducer implements ViewComponentProducer,ViewParamsReporter {

	private static final String NO_PIC_URL = ProfilePicRenderer.NO_PIC_URL;
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
		
		if (userDirectoryService.getCurrentUser().getId() == null || "".equals(userDirectoryService.getCurrentUser().getId()))
			throw new SecurityException("Must be authenticated to view profiles");
		
		
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
				return;
			}
			log.debug("got profile for: " + sPerson.getGivenName() + " " + sPerson.getSurname() + " with id " + sPerson.getAgentUuid());
		} else {
			log.debug("getting profile for current user");
			sPerson = sakaiPersonManager.getSakaiPerson(sakaiPersonManager.getUserMutableType());
		}
		
		//picture stuff
		String picUrl = sPerson.getPictureUrl();
		
		if (picUrl == null || picUrl.trim().length() == 0) {
			picUrl = NO_PIC_URL;
		} else 
			picUrl = sPerson.getPictureUrl();
			
			UIBranchContainer picRow = UIBranchContainer.make(tofill, "isImage:");
			if (sPerson.isSystemPicturePreferred() != null &&  sPerson.isSystemPicturePreferred().booleanValue()) {
				UIInternalLink.make(picRow, "photo", new ImageViewParamaters("imageServlet", sPerson.getAgentUuid()));
			} else if (sPerson.isSystemPicturePreferred() == null || !sPerson.isSystemPicturePreferred().booleanValue() ) {
				UILink.make(picRow, "photo", picUrl);
			} 
		
		String fullname = sPerson.getGivenName() == null ? "" : sPerson.getGivenName();
		fullname += (sPerson.getSurname() != null && sPerson.getSurname()!=null) ? " " : "";
		fullname += sPerson.getSurname()==null ? "" : sPerson.getSurname();
		
		
		UIOutput.make(tofill, "full-name", fullname);
		
		UIOutput.make(tofill, "header-name", fullname);
		
		Boolean hidePInfo = false;
		if (sPerson.getHidePrivateInfo() == null || sPerson.getHidePrivateInfo())
			hidePInfo = true;
		if(sPerson.getMail()!=null)
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
		
		if (sPerson.getNotes() != null) {
			String notes = sPerson.getNotes();
			notes = FormattedText.processFormattedText(notes, new StringBuilder());
			UIVerbatim.make(tofill,"more-info", notes);
		}
		
		
		

	}
	

	public ViewParameters getViewParameters() {
		// TODO Auto-generated method stub
		return new SakaiPersonViewParams();
	}


}
