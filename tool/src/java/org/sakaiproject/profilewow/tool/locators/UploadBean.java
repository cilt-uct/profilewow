package org.sakaiproject.profilewow.tool.locators;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.authz.cover.SecurityService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.profilewow.tool.util.ResourceUtil;
import org.springframework.web.multipart.MultipartFile;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class UploadBean {

	public Map multipartMap;
	private static Log log = LogFactory.getLog(UploadBean.class);
	
	private ResourceUtil resourceUtil;
	public void setResourceUtil(ResourceUtil ru) {
		resourceUtil = ru;
	}

	private ServerConfigurationService serverConfigurationService;
	public void setServerConfigurationService(ServerConfigurationService scs) {
		this.serverConfigurationService = scs;
	}
	
	private static int IMAGE_WIDTH = 150;
	
	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}
	
	private TargettedMessageList messages;
	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}

	public String picUrl = null;
	
	
	public String processUpload() {
		
		log.debug("here we are!");
		log.debug("map of: " + this.multipartMap.size());
		
		Set keySet = multipartMap.keySet();
		Iterator it = keySet.iterator();
		
		while (it.hasNext()) {
			Object key = it.next();
			MultipartFile mapFile = (MultipartFile)multipartMap.get(key);
			long fileSize = mapFile.getSize();
			String fileName = mapFile.getOriginalFilename();
			String type = mapFile.getContentType();
			log.debug(" got file of " + mapFile.getSize() + " of type: " + type );
			//no picture found
			if (mapFile.getSize() == 0 ) {
				//this 
				messages.addMessage(new TargettedMessage("editProfile.photoNotFound", 
						new Object[] {}, TargettedMessage.SEVERITY_ERROR));
				return null;
			}
			
			if (!resourceUtil.isPicture(type)) {
				log.warn("this is not a picture!: " + type);
				messages.addMessage(new TargettedMessage("editProfile.photoTypeInvalid", 
						new Object[] {}, TargettedMessage.SEVERITY_ERROR));
				return null;
			}
			
			// validate the input
			try {
				//resize
				BufferedImage in = ImageIO.read(mapFile.getInputStream());
				BufferedImage out = resize(in);
				String url = resourceUtil.addPicture(fileName, out, type);
				log.debug("got url of " + url);
				SakaiPerson sPerson = spm.getSakaiPerson(spm.getUserMutableType());
				sPerson.setSystemPicturePreferred(new Boolean(false));
				sPerson.setPictureUrl(url);
				spm.save(sPerson);
				
				
				
			} catch (Exception e) {
				log.warn("this is not a real picture!");
				messages.addMessage(new TargettedMessage("editProfile.photoTypeInvalid", new Object[] {}, TargettedMessage.SEVERITY_ERROR));
				return "invalid";
			}
			
		}
		messages.addMessage(new TargettedMessage("editProfile.photoUploaded", new Object[] {}, TargettedMessage.SEVERITY_INFO));
		return "success";
	}
	
	public void useOfficial() {
		log.info("useOfficial()");
		SakaiPerson sp = spm.getSakaiPerson(spm.getUserMutableType());
		sp.setSystemPicturePreferred(new Boolean(true));
		spm.save(sp);
	}
	
	public String changePicture() {
		log.info("changing the picture!" + this.picUrl);
		
		SakaiPerson sPerson = spm.getSakaiPerson(spm.getUserMutableType());
		sPerson.setSystemPicturePreferred(new Boolean(false));
		sPerson.setPictureUrl(picUrl);
		spm.save(sPerson);
		
		messages.addMessage(new TargettedMessage("editProfile.photoChanged", new Object[] {}, TargettedMessage.SEVERITY_INFO));
		return "success";
	}
	
	
	private BufferedImage resize(BufferedImage img) throws Exception {

		if (img.getWidth() <= 75)
			return img;
	
		
		log.info("image of w: " + img.getWidth() + " h: " + img.getHeight());
		//we need to keep the aspect ratio
		float ratio =  (float)img.getHeight()/(float)img.getWidth();
		
		float newH = IMAGE_WIDTH * ratio;
		log.info("ratio: " + ratio + " newH: " + newH);
		
		
		return resize(img, IMAGE_WIDTH, (int)newH);
	}
	
	
	private BufferedImage resize(BufferedImage img, int newW, int newH) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return dimg;
	}
	
	

}
