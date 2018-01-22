package org.sakaiproject.profilewow.tool.producers;

import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.profilewow.tool.producers.templates.PasswordFormRenderer;
import org.sakaiproject.profilewow.tool.producers.templates.ProfilePicRenderer;
import org.sakaiproject.profilewow.tool.producers.templates.SearchBoxRenderer;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.util.FormattedText;

import lombok.extern.slf4j.Slf4j;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

@Slf4j
public class MainProducer implements ViewComponentProducer, DefaultView {

	
	
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
	
	private ProfilePicRenderer profilePicRenderer;
	public void setProfilePicRenderer(ProfilePicRenderer profilePicRenderer) {
		this.profilePicRenderer = profilePicRenderer;
	}
	
	private PasswordFormRenderer passwordFormRenderer;
	public void setPasswordFormRenderer(PasswordFormRenderer passwordFormRenderer) {
		this.passwordFormRenderer = passwordFormRenderer;
	}


	private SearchBoxRenderer searchBoxRenderer;
	public void setSearchBoxRenderer(SearchBoxRenderer searchBoxRenderer) {
		this.searchBoxRenderer = searchBoxRenderer;
	}


	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		SakaiPerson sPerson = spm.getSakaiPerson(spm.getUserMutableType());
		if (sPerson == null) {
			log.debug("creating a new profile!");
			sPerson = spm.create(userDirectoryService.getCurrentUser().getId(), spm.getUserMutableType());
			//populate the usename and password
			User u = userDirectoryService.getCurrentUser();
			sPerson.setGivenName(u.getFirstName());
			sPerson.setSurname(u.getLastName());
			sPerson.setMail(u.getEmail());
			spm.save(sPerson);
		}

		//makeProfilePic(tofill, sPerson); 
		profilePicRenderer.makeProfilePic(tofill, "profile-pic:", sPerson);
		passwordFormRenderer.renderPasswordForm(tofill, "passForm:", sPerson);
		
		searchBoxRenderer.renderSearchBox(tofill, "search:");
		
		//edit link
		UIInternalLink.make(tofill, "editProfileLink",  UIMessage.make("editProfileLink"), new SimpleViewParameters(EditProducer.VIEW_ID));
		

		
		log.debug("got profile for: " + sPerson.getGivenName() + " " + sPerson.getSurname());
		log.debug("uuid: " + sPerson.getUid() + ", agent_uuid: " + sPerson.getAgentUuid());
		
		
		String fullname = sPerson.getGivenName() == null ? "" : sPerson.getGivenName();
		fullname += (sPerson.getSurname() != null && sPerson.getSurname()!=null) ? " " : "";
		fullname += sPerson.getSurname()==null ? "" : sPerson.getSurname();
		
		UIOutput.make(tofill,"full-name", fullname);
		String email = sPerson.getMail();
		if (email != null && !"".equals(email))
			UILink.make(tofill,"email", email, "mailto:"+email);
		// not sure what this is meant to be UIOutput.make(tofill,"position", sPerson.getPosition());
		if (sPerson.getOrganizationalUnit() != null)
			UIOutput.make(tofill,"department", sPerson.getOrganizationalUnit());
		
		if (sPerson.getCampus() != null)
			UIOutput.make(tofill,"school", sPerson.getCampus());
		if (sPerson.getRoomNumber() != null)
			UIOutput.make(tofill,"room", sPerson.getRoomNumber());
		
		if (sPerson.getTelephoneNumber() != null)
			UIOutput.make(tofill,"workphone", sPerson.getTelephoneNumber());
		if(sPerson.getMobile() != null)
			UIOutput.make(tofill,"mobile", sPerson.getMobile());
		
	
		if (sPerson.getNotes() != null) {
			String notes = sPerson.getNotes();
			notes = FormattedText.processFormattedText(notes, new StringBuilder());
			UIVerbatim.make(tofill,"moreinfo", notes);
		}
		
		
		if (sPerson.getLabeledURI() != null)
			UILink.make(tofill, "homepage", sPerson.getLabeledURI(),sPerson.getLabeledURI());

	}

}
