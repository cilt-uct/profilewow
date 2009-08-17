package org.sakaiproject.profilewow.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.profilewow.tool.producers.templates.PasswordFormRenderer;
import org.sakaiproject.profilewow.tool.producers.templates.ProfilePicRenderer;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.components.UISelectLabel;
import uk.org.ponder.rsf.components.decorators.UILabelTargetDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class EditProducer implements ViewComponentProducer, NavigationCaseReporter {

	public static final String VIEW_ID = "editProfile";
	
	private static Log log = LogFactory.getLog(EditProducer.class);
	public String getViewID() {
		return VIEW_ID;
	}

	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}

	private MessageLocator messageLocator;
	public void setMessageLocator(MessageLocator messageLocator) {

		this.messageLocator = messageLocator;
	}

	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}

	private SecurityService securityService;

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	private ToolManager toolManager;
	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
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

	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		//process any messages
		
		if (tml.size() > 0) {
			for (int i = 0; i < tml.size(); i ++ ) {
				UIBranchContainer errorRow = UIBranchContainer.make(tofill,"error-row:",  Integer.valueOf(i).toString());
				//if (tml.messageAt(i).args != null ) {	    		
					//UIMessage.make(errorRow,"error",tml.messageAt(i).acquireMessageCode(),(String[])tml.messageAt(i).args[0]);
				//} else {
					UIMessage.make(errorRow,"error",tml.messageAt(i).acquireMessageCode());
				//}

			}
		}


		
		UIForm form = UIForm.make(tofill,"edit-form");

		SakaiPerson sPerson = spm.getSakaiPerson(spm.getUserMutableType());
		if (sPerson == null) {
			log.debug("creating a new profile!");
			sPerson = spm.create(userDirectoryService.getCurrentUser().getId(), spm.getUserMutableType());
			spm.save(sPerson);
		}

		log.debug("got profile for: " + sPerson.getGivenName() + " " + sPerson.getSurname());
		log.debug("uuid: " + sPerson.getUid() + ", agent_uuid: " + sPerson.getAgentUuid());

		
		//makeProfilePic(tofill, sPerson); 
		profilePicRenderer.makeProfilePic(tofill, "profile-pic:", sPerson);
		passwordFormRenderer.renderPasswordForm(tofill, "passForm:", sPerson);
		
		
		String otpBean = "profileBeanLocator." + sPerson.getUid() + ".sakaiPerson";

		
		boolean enableEdit = this.canEditeName(userDirectoryService.getCurrentUser());
		if (!enableEdit) {
			//fName.decorators = new DecoratorList(new UIDisabledDecorator(true));
			//lName.decorators = new DecoratorList(new UIDisabledDecorator(true));
			UIOutput.make(form, "firstname-text",sPerson.getGivenName());
			UIOutput.make(form, "lastname-text", sPerson.getSurname());
			form.parameters.add(new UIELBinding(otpBean + ".givenName" ,sPerson.getGivenName()));
			form.parameters.add(new UIELBinding(otpBean + ".surname", sPerson.getSurname()));
		} else {
			UIInput.make(form,"editProfileForm-first_name", otpBean + ".givenName" ,sPerson.getGivenName());
			UIInput.make(form,"editProfileForm-lname", otpBean + ".surname", sPerson.getSurname());
		}

	
		
		UIInput.make(form,"editProfileForm-nickname", otpBean + ".nickname", sPerson.getNickname());
		UIInput.make(form,"editProfileForm-position", otpBean + ".affiliation", sPerson.getAffiliation());
		UIInput.make(form,"editProfileForm-department", otpBean + ".organizationalUnit", sPerson.getOrganizationalUnit());
		UIInput.make(form,"editProfileForm-school", otpBean + ".campus", sPerson.getCampus());
		UIInput.make(form,"editProfileForm-room", otpBean + ".roomNumber", sPerson.getRoomNumber());
		
		String mail = sPerson.getMail();
		if (mail == null)
			mail = "";
		if (canEditeEmail(userDirectoryService.getCurrentUser())) {
			UIInput.make(form,"editProfileForm-email", otpBean + ".mail", mail);
		} else {
			UIOutput.make(form, "emailNoEdit", mail);
			form.parameters.add(new UIELBinding(otpBean + ".mail" ,mail));
		}
		UIInput.make(form,"editProfileForm-title", otpBean + ".title", sPerson.getTitle());
		//not in profile data yet
		UIInput.make(form,"editProfileForm-country", otpBean + ".localityName", sPerson.getLocalityName());

		UIInput.make(form,"editProfileForm-homepage", otpBean + ".labeledURI", sPerson.getLabeledURI());
		UIInput.make(form,"editProfileForm-workphone", otpBean + ".telephoneNumber", sPerson.getTelephoneNumber());
		UIInput.make(form,"editProfileForm-mobile", otpBean + ".mobile", sPerson.getMobile());
		UIInput.make(form,"editProfileForm-more", otpBean + ".notes", sPerson.getNotes());

		//validate messages
		
		UIOutput.make(form, "validateRequired", messageLocator.getMessage("validate.required"));
		UIOutput.make(form, "validateEmail", messageLocator.getMessage("validate.email"));
		UIOutput.make(form, "validateUrl", messageLocator.getMessage("validate.url"));
		UIOutput.make(form, "validateDate", messageLocator.getMessage("validate.date"));
		UIOutput.make(form, "validateMinlength", messageLocator.getMessage("validate.minlength"));

		//Privacy settings
		String hideS = "false";
		String hideS2 = "true";
		if (sPerson.getHidePrivateInfo()!=null && sPerson.getHidePrivateInfo().booleanValue()) {
			hideS = "true";
		}

		log.debug("hide personal is " + hideS);

		UISelect hide = UISelect.make(form, "hide-select",new String[]{"true", "false"},
				new String[]{messageLocator.getMessage("editProfile.sms.yes"), messageLocator.getMessage("editProfile.sms.no")}, 
				otpBean + ".hidePrivateInfo", hideS);

		String hideID = hide.getFullID();

		for(int i = 0; i < 2; i ++ ) {
			UIBranchContainer radiobranch = UIBranchContainer.make(form,
					"hideSelect:", Integer.valueOf(i).toString());
			UISelectChoice choice = UISelectChoice.make(radiobranch, "editProfile-hide", hideID, i);
			UISelectLabel lb = UISelectLabel.make(radiobranch, "hide-label", hideID, i);
			UILabelTargetDecorator.targetLabel(lb, choice);
		}

		UISelect hide2 = UISelect.make(form, "hide-select-info",new String[]{"true", "false"},
				new String[]{messageLocator.getMessage("editProfile.sms.yes"), messageLocator.getMessage("editProfile.sms.no")}, 
				otpBean + ".hidePublicInfo", hideS2);

		String hideID2 = hide2.getFullID();

		for(int i = 0; i < 2; i ++ ) {
			UIBranchContainer radiobranch = UIBranchContainer.make(form,
					"hideSelect-info:", Integer.valueOf(i).toString());
			UISelectChoice choice = UISelectChoice.make(radiobranch, "editProfile-hideInfo", hideID2, i);
			UISelectLabel lb = UISelectLabel.make(radiobranch, "hide-label-info", hideID2, i);
			UILabelTargetDecorator.targetLabel(lb, choice);
		}


		//sms preference

		UISelect sms = UISelect.make(form, "sms-select",new String[]{"true", "false"},
				new String[]{messageLocator.getMessage("editProfile.sms.yes"), messageLocator.getMessage("editProfile.sms.no")}, 
				"profileBeanLocator." + sPerson.getUid() + ".smsNotifications", receiveSMSNotifications().toString());
		String selectID = sms.getFullID();

		for(int i = 0; i < 2; i ++ ) {
			UIBranchContainer radiobranch = UIBranchContainer.make(form,
					"smsSelect:", Integer.valueOf(i).toString());
			UISelectChoice choice = UISelectChoice.make(radiobranch, "editProfileForm-sms", selectID, i);
			UISelectLabel lb = UISelectLabel.make(radiobranch, "smslabel", selectID, i);
			UILabelTargetDecorator.targetLabel(lb, choice);
		}
		//UIInput sms = UIInput.make(form, "editProfileForm-sms", "profileBeanLocator." + sPerson.getUid() + ".smsNotifications", "true");

		//UIMessage.make(form, "smslabel","editProfile.sms");



		UICommand.make(form, "profile-save","Save","profileBeanLocator.saveAll");


		//UIInternalLink.make(tofill, "change-pic", messageLocator.getMessage("editProfile.changePic"), new SimpleViewParameters(ChangePicture.VIEW_ID));
		UIInternalLink.make(tofill, "upload-pic", "editProfile.uploadPic");		

	}


	
	private boolean canEditeName(User u) {
		if (securityService.unlock(UserDirectoryService.SECURE_UPDATE_USER_OWN_NAME, "/site/" + toolManager.getCurrentPlacement().getContext())) {
			log.debug("user can change name");
			return true;
		}
		return false;		
	
		
	}
	
	private boolean canEditeEmail(User u) {
		if (securityService.unlock(UserDirectoryService.SECURE_UPDATE_USER_OWN_EMAIL, "/site/" + toolManager.getCurrentPlacement().getContext())) {
			log.debug("user can change email");
			return true;
		}
		return false;		
	
		
	}

	private Boolean receiveSMSNotifications() {
		User u = userDirectoryService.getCurrentUser();
		ResourceProperties rp = u.getProperties();

		String val = rp.getProperty("smsnotifications");

		log.debug("got sms notification of: " + val );

		// Default to true if unset (i.e. opt-in)
		return (val == null) ? Boolean.valueOf(true) : Boolean.valueOf(val);
	}

	public List reportNavigationCases() {
		 List<NavigationCase> togo = new ArrayList<NavigationCase> (); // Always navigate back to this view.
		 togo.add(new NavigationCase(null, new SimpleViewParameters(MainProducer.VIEW_ID)));
		return togo;
	}

}
