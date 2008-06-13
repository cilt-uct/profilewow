package org.sakaiproject.profilewow.tool.facade;

import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

public class SakaiPersonFacade {

	
	private String userId;
	private SakaiPerson sakaiPerson;
	public String[] smsNotifications;
	
	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}
	
	public SakaiPersonFacade(String userEid) {
		
		
		//we need the userID
		try {
			this.userId = userDirectoryService.getUserId(userEid);
			//and get the SakaiPerson here
			this.sakaiPerson = spm.getSakaiPerson(userId, spm.getUserMutableType());
		} catch (UserNotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public SakaiPersonFacade() {
		
	}
	
	public SakaiPerson getSakaiPerson() {
		return sakaiPerson;
	}
	public void setSakaiPerson(SakaiPerson sakaiPerson) {
		this.sakaiPerson = sakaiPerson;
	}
	
	/*
	public String[] getSmsNotifications() {
		return smsNotifications;
	}
	
	public void setSmsNotifications(String[] smsNotifications) {
		this.smsNotifications = smsNotifications;
	}
	*/
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
	
}
