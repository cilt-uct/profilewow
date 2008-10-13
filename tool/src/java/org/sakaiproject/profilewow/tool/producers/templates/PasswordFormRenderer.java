package org.sakaiproject.profilewow.tool.producers.templates;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;

public class PasswordFormRenderer {
	private static Log log = LogFactory.getLog(PasswordFormRenderer.class);

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

	
	public void renderPasswordForm(UIContainer tofill, String divId, SakaiPerson sPerson) {
		if (canChangePassword(userDirectoryService.getCurrentUser())) {
			UIJointContainer join = new UIJointContainer(tofill, divId, "passTemplate:");
			UIForm passForm = UIForm.make(join, "passForm:");
			UIInput.make(passForm,"pass1","userBeanLocator." + sPerson.getUid() + ".passOne");
			UIInput.make(passForm,"pass2","userBeanLocator." + sPerson.getUid() + ".passTwo");
			//form.parameters.add(new UIELBinding("userBeanLocator." + )
			UICommand.make(passForm, "passSubmit", "userBeanLocator.saveAll");
		}	
	}
	
	
	
	private boolean canChangePassword(User u) {
		
		if (securityService.unlock(UserDirectoryService.SECURE_UPDATE_USER_OWN_PASSWORD, "/site/" + toolManager.getCurrentPlacement().getContext())) {
			log.debug("user can set password");
			return true;
		}
		return false;		
		
	}
}
