package org.sakaiproject.profilewow.tool.locators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.user.api.UserAlreadyDefinedException;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserEdit;
import org.sakaiproject.user.api.UserLockedException;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.api.UserPermissionException;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class UserBeanLocator implements BeanLocator{

private static Log log = LogFactory.getLog(UserBeanLocator.class);
	
	private Map<String, Object> delivered = new HashMap<String, Object>();
	public static final String NEW_PREFIX = "new ";
	public static String NEW_1 = NEW_PREFIX + "1";
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}
	
	private TargettedMessageList messages;
	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}

	private UserPassword userPassword;
	
	public Object locateBean(String name) {
		 Object togo=delivered.get(name);
			if (togo == null){
				if(name.startsWith(NEW_PREFIX)){
					// we shouldn't need this
				}
				else { 
					log.debug("looking for user: " + name);
					if (userPassword == null) {
						userPassword = new UserPassword();
					}
					
					togo = userPassword;
				}
				delivered.put(name, togo);
			}
			return togo;
	}

	
	public void saveAll() {
		log.debug("About to set new password");
		for (Iterator<String> it = delivered.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			log.debug("got key: " + key);
			UserPassword up = (UserPassword) delivered.get(key);
			
			if (up.getPassOne() == null || up.getPassOne().length() == 0 || up.getPassTwo() == null || up.getPassTwo().length() == 0 ) {
				//both must be filled in
				messages.addMessage( new TargettedMessage("password.empty",
			               new Object[] { "password field empty" }, 
			               TargettedMessage.SEVERITY_ERROR));
				return;
				
			}
			
			//do the passwords match?
			if (!up.getPassOne().equals(up.getPassTwo())) {
				log.warn("paswords do not match");
				messages.addMessage( new TargettedMessage("passwords.notMatch",
			               new Object[] { "passwords don't match" }, 
			               TargettedMessage.SEVERITY_ERROR));
				return;
			}
			
			
			log.debug("about to reset password");
			try {
				UserEdit ue = userDirectoryService.editUser(userDirectoryService.getCurrentUser().getId());
				ue.setPassword(up.getPassOne());
				userDirectoryService.commitEdit(ue);
				log.debug("password updated for "  + key);
				messages.addMessage( new TargettedMessage("passwords.updated",
			               new Object[] { "password has been updated" }, 
			               TargettedMessage.SEVERITY_INFO));
				
			} catch (UserNotDefinedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserPermissionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserLockedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserAlreadyDefinedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}
	}
	

	
	public class UserPassword {
		private String id;
		private String pass1;
		private String pass2;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getPassOne() {
			return pass1;
		}
		public void setPassOne(String pass1) {
			this.pass1 = pass1;
		}
		public String getPassTwo() {
			return pass2;
		}
		public void setPassTwo(String pass2) {
			this.pass2 = pass2;
		}
	}
	
	
	
}
