package org.sakaiproject.profilewow.tool.locators;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.profilewow.tool.util.ResourceUtil;
import org.springframework.web.multipart.MultipartFile;

import uk.org.ponder.messageutil.TargettedMessageList;

public class UploadBean {

	public Map multipartMap;
	private static Log log = LogFactory.getLog(UploadBean.class);
	
	private ResourceUtil resourceUtil;
	public void setResourceUtil(ResourceUtil ru) {
		resourceUtil = ru;
	}
	private TargettedMessageList tml;
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.tml = tml;
	}
	private ServerConfigurationService serverConfigurationService;
	public void setServerConfigurationService(ServerConfigurationService scs) {
		this.serverConfigurationService = scs;
	}
	
	
	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}
	
	
	
	
	public void processUpload() {
		
		log.info("here we are!");
		log.info("map of: " + this.multipartMap.size());
		
		Set keySet = multipartMap.keySet();
		Iterator it = keySet.iterator();
		
		while (it.hasNext()) {
			Object key = it.next();
			MultipartFile mapFile = (MultipartFile)multipartMap.get(key);
			long fileSize = mapFile.getSize();
			String fileName = mapFile.getOriginalFilename();
			String type = mapFile.getContentType();
			log.info(" got file of " + mapFile.getSize() + "of type: " + type );
			
			
			if (!resourceUtil.isPicture(type)) {
				log.warn("this is not a picture!: " + type);
			}
			
			// validate the input
			
			
			int maxPictureSize = serverConfigurationService.getInt("tool.profile.ProfileTool.maxPictureSize", 1024);
			if(fileSize > maxPictureSize*1024) {
				log.warn("File uploaded was too large (" + fileSize + " bytes " + maxPictureSize + "max): " + fileName);
			}
			

			
			try {
				String url = resourceUtil.addPicture(fileName, mapFile.getBytes(), type);
				log.info("got url of " + url);
				SakaiPerson sPerson = spm.getSakaiPerson(spm.getUserMutableType());
				sPerson.setSystemPicturePreferred(new Boolean(false));
				sPerson.setPictureUrl(url);
				spm.save(sPerson);
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}
