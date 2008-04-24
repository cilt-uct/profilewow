package org.sakaiproject.profilewow.tool.facade;

import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;

public class SakaiPersonFacade {

	
	private String userId;
	private SakaiPerson sakaiPerson;
	public String[] smsNotifications;
	
	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}
	
	public SakaiPersonFacade(String userId) {
		this.userId = userId;
		//and get the SakaiPerson here
		this.sakaiPerson = spm.getSakaiPerson(userId, spm.getUserMutableType());
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
