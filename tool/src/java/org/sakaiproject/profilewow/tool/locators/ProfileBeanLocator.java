package org.sakaiproject.profilewow.tool.locators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.profilewow.tool.facade.SakaiPersonFacade;
import org.sakaiproject.user.api.UserAlreadyDefinedException;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserEdit;
import org.sakaiproject.user.api.UserLockedException;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.api.UserPermissionException;
import org.sakaiproject.util.FormattedText;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class ProfileBeanLocator implements BeanLocator {

	
	private static Log log = LogFactory.getLog(ProfileBeanLocator.class);
	
	private Map delivered = new HashMap();
	public static final String NEW_PREFIX = "new ";
	public static String NEW_1 = NEW_PREFIX + "1";
	
	private TargettedMessageList messages;
	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}


	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}
	
	private SakaiPersonFacade sPersonFacade;
	public void setSakaiPersonFacade(SakaiPersonFacade spf) {
		this.sPersonFacade = spf;
	}
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}
	
	public Object locateBean(String name) {
//		 TODO Auto-generated method stub
		Object togo=delivered.get(name);
		if (togo == null){
			if(name.startsWith(NEW_PREFIX)){
				// we shouldn't need this
			}
			else { 
				log.debug("looking for user: " + name);
				if (sPersonFacade == null) {
					sPersonFacade = new SakaiPersonFacade(name);
					
				} 
				if (sPersonFacade.getUserId() == null) {
					sPersonFacade.setUserId(name);
				}
				
				if (sPersonFacade.getSakaiPerson() == null) {
					sPersonFacade.setSakaiPerson(spm.getSakaiPerson(name, spm.getUserMutableType()));
				}
				
				
				togo = sPersonFacade;
			}
			delivered.put(name, togo);
		}
		return togo;
	}

	public boolean remove(String beanname) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public void set(String beanname, Object toset) {
		throw new UnsupportedOperationException("Not implemented");
		
	}
	
	public void saveAll() {
		log.debug("Savind all!");
		for (Iterator<String> it = delivered.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			log.debug("got key: " + key);
			SakaiPersonFacade person = (SakaiPersonFacade) delivered.get(key);
			
			SakaiPerson sperson = person.getSakaiPerson();
			if (sperson.getGivenName() == null || sperson.getGivenName().length() == 0) {
				
				messages.addMessage( new TargettedMessage("givenName.empty",
			               new Object[] { "given name empty" }, 
			               TargettedMessage.SEVERITY_ERROR));
				return;
			}
			
			if (sperson.getSurname() == null || sperson.getSurname().length() == 0) {
				messages.addMessage( new TargettedMessage("surName.empty",
			               new Object[] { "surname empty" }, 
			               TargettedMessage.SEVERITY_ERROR));
				
				return;
			}
			
			
			if (sperson.getMail() == null || !isValidMail(sperson.getMail())) {
				
				messages.addMessage( new TargettedMessage("email.invalid",
			               new Object[] { "invalid email"}, 
			               TargettedMessage.SEVERITY_ERROR));
				
				return;
			}
			
			String notes = sperson.getNotes();
			notes = FormattedText.escapeHtmlFormattedText(notes);
			sperson.setNotes(notes);
			
			spm.save(person.getSakaiPerson());
			
			log.debug("sms preference is: " + person.smsNotifications);
			
			Boolean setValue = new Boolean("false");
			if (person.smsNotifications != null) {
				if (person.smsNotifications[0] != null)
					setValue = (new Boolean(person.smsNotifications[0]));
				try {
					UserEdit ue = userDirectoryService.editUser(person.getUserId());
					ResourcePropertiesEdit rpe = ue.getPropertiesEdit();
					String PROPERTY_NAME = "smsnotifications";
					rpe.removeProperty(PROPERTY_NAME);
					rpe.addProperty(PROPERTY_NAME, setValue.toString());
					userDirectoryService.commitEdit(ue);



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
		messages.addMessage( new TargettedMessage("editProfile.profileSaved",
	               new Object[] { "profile saved" }, 
	               TargettedMessage.SEVERITY_INFO));
	}
	
	public String useOfficialPic(){
		SakaiPerson sp = spm.getSakaiPerson(spm.getUserMutableType());
		log.debug("setting picture preffered for " + sp.getGivenName());
		sp.setSystemPicturePreferred(new Boolean(true));
		spm.save(sp);
		
		
		return "success!";
	}

	
	private boolean isValidMail(String email) {
		
		// TODO: Use a generic Sakai utility class (when a suitable one exists)
		
		if (email == null || email.equals(""))
			return false;
		
		email = email.trim();
		//must contain @
		if (email.indexOf("@") == -1)
			return false;
		
		//an email can't contain spaces
		if (email.indexOf(" ") > 0)
			return false;
		
		//"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*$" 
		if (email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*$")) 
			return true;
	
		log.warn(email + " is not a valid eamil address");
		return false;
	}
}
