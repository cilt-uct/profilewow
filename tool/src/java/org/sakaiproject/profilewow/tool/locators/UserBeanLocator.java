package org.sakaiproject.profilewow.tool.locators;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

import uk.org.ponder.beanutil.BeanLocator;

public class UserBeanLocator implements BeanLocator{

private static Log log = LogFactory.getLog(UserBeanLocator.class);
	
	private Map delivered = new HashMap();
	public static final String NEW_PREFIX = "new ";
	public static String NEW_1 = NEW_PREFIX + "1";
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}
	
	private UserPassword userPassword;
	
	public Object locateBean(String name) {
		 Object togo=delivered.get(name);
			if (togo == null){
				if(name.startsWith(NEW_PREFIX)){
					// we shouldn't need this
				}
				else { 
					log.info("looking for user: " + name);
					if (userPassword == null) {
						userPassword = new UserPassword();
					}
					
					togo = userPassword;
				}
				delivered.put(name, togo);
			}
			return togo;
	}

	
	private void saveAll() {
		log.info("About to set new password");
	}
	
	
	private class UserPassword {
		private String id;
		private String pass1;
		private String pass2;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getPass1() {
			return pass1;
		}
		public void setPass1(String pass1) {
			this.pass1 = pass1;
		}
		public String getPass2() {
			return pass2;
		}
		public void setPass2(String pass2) {
			this.pass2 = pass2;
		}
	}
	
}
