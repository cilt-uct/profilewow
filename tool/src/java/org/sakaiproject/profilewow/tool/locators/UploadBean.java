package org.sakaiproject.profilewow.tool.locators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

public class UploadBean {

	public Map multipartMap;
	private static Log log = LogFactory.getLog(UploadBean.class);
	
	
	public void processUpload() {
		
		log.info("here we are!");
		log.info("map of: " + this.multipartMap.size());
		
		Set keySet = multipartMap.keySet();
		Iterator it = keySet.iterator();
		
		while (it.hasNext()) {
			Object key = it.next();
			MultipartFile mapFile = (MultipartFile)multipartMap.get(key);
			log.info(" got file of " + mapFile.getSize());
			
		}
	}
	
}
