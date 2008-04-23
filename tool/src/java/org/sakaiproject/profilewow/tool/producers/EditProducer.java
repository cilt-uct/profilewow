package org.sakaiproject.profilewow.tool.producers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.profilewow.tool.params.ImageViewParamaters;
import org.sakaiproject.user.api.UserDirectoryService;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class EditProducer implements ViewComponentProducer, DefaultView {

	public static final String VIEW_ID = "edit";
	
	private static Log log = LogFactory.getLog(EditProducer.class);
	public String getViewID() {
		// TODO Auto-generated method stub
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
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		// TODO Auto-generated method stub
		
		UIForm form = UIForm.make(tofill,"edit-form");
		
		SakaiPerson sPerson = spm.getSakaiPerson(spm.getUserMutableType());
		if (sPerson == null) {
			sPerson = spm.create(userDirectoryService.getCurrentUser().getId(), spm.getUserMutableType());
			spm.save(sPerson);
		}
		
		log.info("got profile for: " + sPerson.getGivenName() + " " + sPerson.getSurname());
		
		String otpBean = "profileBeanLocator." + sPerson.getUid();
		UIInput.make(form,"editProfileForm-first_name", otpBean + ".givenName" ,sPerson.getGivenName());
		UIInput.make(form,"editProfileForm-lname", otpBean + ".surname", sPerson.getSurname());
		UIInput.make(form,"editProfileForm-nickname", otpBean + ".nickname", sPerson.getNickname());
		UIInput.make(form,"editProfileForm-position", otpBean + ".title", sPerson.getTitle());
		UIInput.make(form,"editProfileForm-department", otpBean + ".organizationalUnit",sPerson.getOrganizationalUnit());
		UIInput.make(form,"editProfileForm-school", otpBean + ".campus", sPerson.getCampus());
		UIInput.make(form,"editProfileForm-room", otpBean + ".roomNumber", sPerson.getRoomNumber());
		UIInput.make(form,"editProfileForm-email", otpBean + ".mail", sPerson.getMail());
		
		
		//picture stuff
		String picUrl = sPerson.getPictureUrl();
		if (picUrl == null || picUrl.trim().length() == 0)
			picUrl = "../images/pictureUnavailable.jpg";
		
		UILink.make(form, "manual-pic", picUrl);
		if (sPerson.getPicturePrefered() == null ||  sPerson.getPicturePrefered().intValue() == SakaiPerson.NO_PICTURE_PREFERED) {
			UILink.make(form, "current-pic", "../images/pictureUnavailable.jpg");
		} else if (sPerson.getPicturePrefered().intValue() == SakaiPerson.CUSTOM_PICTURE_PREFERED) {
			UILink.make(form, "current-pic", "picUrl");
		} else {
			UIInternalLink.make(form, "current-pic", new ImageViewParamaters("imageServlet", sPerson.getUuid()));
		}
		
		UIInternalLink.make(form, "official-pic", new ImageViewParamaters("imageServlet", sPerson.getUuid()));
		
		
		
		UICommand.make(form, "profile-save","save","profileBeanLocator.saveAll");
		
		UIInternalLink.make(form, "change-pic", messageLocator.getMessage("editProfile.changePic"), new SimpleViewParameters(ChangePicture.VIEW_ID));
		
	}
	
	
	/**
	 * Returns String for image. Uses the config bundle
	 * to return paths to not available images.  
	 */
	private String getUrlOfficialPicture(String userId) {
		
		String imageUrl = "";
			
		imageUrl = "ProfileImageServlet.prf?photo=" + userId;
			
		return imageUrl; 
	}

	
	
}
