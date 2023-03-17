package org.sakaiproject.profilewow.tool.locators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.profilewow.tool.facade.SakaiPersonFacade;
import org.sakaiproject.sms.logic.external.NumberRoutingHelper;
import org.sakaiproject.user.api.UserAlreadyDefinedException;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserEdit;
import org.sakaiproject.user.api.UserLockedException;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.api.UserPermissionException;
import org.sakaiproject.util.api.FormattedText;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

@Slf4j
public class ProfileBeanLocator implements BeanLocator {

	
	private Map<String, Object> delivered = new HashMap<>();
	public static final String NEW_PREFIX = "new ";
	public static String NEW_1 = NEW_PREFIX + "1";
	
	@Setter private FormattedText formattedText;
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
	
	private NumberRoutingHelper numberRoutingHelper;
	public void setNumberRoutingHelper(NumberRoutingHelper numberRoutingHelper) {
		this.numberRoutingHelper = numberRoutingHelper;
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

		log.info("saveAll()");

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

			if (StringUtils.isNotEmpty(sperson.getLabeledURI()) && !isValidUrl(sperson.getLabeledURI())) {
				messages.addMessage( new TargettedMessage("url.invalid",
			               new Object[] { "invalid url"},
			               TargettedMessage.SEVERITY_ERROR));
				return;
			}
			
			String notes = sperson.getNotes();
			notes = formattedText.processFormattedText(notes, new StringBuilder());
			sperson.setNotes(notes);
			
			//set the normalized mobile No
			String mobile = person.getSakaiPerson().getMobile();
			if (mobile != null) {
				String normalized = numberRoutingHelper.normalizeNumber(mobile);
				person.getSakaiPerson().setNormalizedMobile(normalized);
			}
			
			spm.save(person.getSakaiPerson());
			
			//log.debug("sms preference is: " + person.smsNotifications);
			
			Boolean setValue = Boolean.valueOf("true");
			if (person.smsNotifications != null) {
				if (person.smsNotifications[0] != null)
					setValue = (Boolean.valueOf(person.smsNotifications[0]));
				try {
					UserEdit ue = userDirectoryService.editUser(person.getUserId());
					ResourcePropertiesEdit rpe = ue.getPropertiesEdit();
					String PROPERTY_NAME = "smsnotifications";
					rpe.removeProperty(PROPERTY_NAME);
					rpe.addProperty(PROPERTY_NAME, setValue.toString());
					userDirectoryService.commitEdit(ue);
				} catch (UserNotDefinedException e) {
					log.warn(e.getMessage(), e);
				} catch (UserPermissionException e) {
					log.warn(e.getMessage(), e);
				} catch (UserLockedException e) {
					log.warn(e.getMessage(), e);
				} catch (UserAlreadyDefinedException e) {
					log.warn(e.getMessage(), e);
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
		sp.setSystemPicturePreferred(Boolean.valueOf(true));
		spm.save(sp);
		
		return "success!";
	}

	private boolean isValidMail(String email) {
		if (email == null || email.equals(""))
			return false;
		
		email = email.trim();
		EmailValidator ev = EmailValidator.getInstance();
		boolean email_valid = ev.isValid(email);
		log.info("Email {} valid? {}", email, email_valid);

		return email_valid;
	}

	private boolean isValidUrl(String url) {
		if (url == null || url.equals(""))
			return false;

		String[] schemes = {"http","https"};
		UrlValidator urlValidator = new UrlValidator(schemes);

		url = url.trim();
		boolean url_valid = urlValidator.isValid(url);
		log.info("Url {} valid? {}", url, url_valid);

		return url_valid;
	}
}
