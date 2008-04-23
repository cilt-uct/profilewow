package org.sakaiproject.profilewow.tool.locators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;


import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.WriteableBeanLocator;
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
	
	public Object locateBean(String name) {
//		 TODO Auto-generated method stub
		Object togo=delivered.get(name);
		if (togo == null){
			if(name.startsWith(NEW_PREFIX)){
				// we shouldn't need this
			}
			else { 
				log.info("looking for user: " + name);
				togo = spm.getSakaiPerson(name, spm.getUserMutableType());
				if (togo == null)
					log.warn("no profile for:  " + name);
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
		log.info("Savind all!");
		for (Iterator<String> it = delivered.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			log.info("got key: " + key);
			SakaiPerson person = (SakaiPerson) delivered.get(key);
			spm.save(person);
		}
		
	}
}
