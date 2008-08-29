package org.sakaiproject.profilewow.tool.images;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.profilewow.tool.params.ImageViewParamaters;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.user.api.UserDirectoryService;





import uk.org.ponder.rsf.processor.HandlerHook;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.UniversalRuntimeException;

public class ImageHandlerHook implements HandlerHook {

	private static Log log = LogFactory.getLog(ImageHandlerHook.class);

	private HttpServletResponse response;
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}


	private ViewParameters viewparams;
	public void setViewparams(ViewParameters viewparams) {
		this.viewparams = viewparams;
	}

	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}

	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}
	
	private SessionManager sessionManager;
	public void setSessionManager(SessionManager sm) {
		sessionManager = sm;
	}
	
	
	public boolean handle() {
		// TODO Auto-generated method stub

		ImageViewParamaters ivp;
		if (viewparams instanceof ImageViewParamaters) {
			ivp = (ImageViewParamaters) viewparams;
			log.debug("got a ImageView");
		} else {
			log.debug("Not an image view!: " + viewparams);
			return false;
		}
		OutputStream resultsOutputStream = null;
		try {
			resultsOutputStream = response.getOutputStream(); 
		}
		catch (IOException ioe) {
			throw UniversalRuntimeException.accumulate(ioe, "Unable to get response stream for Profile image export");
		}


		// Response Headers that are the same for all Output types
		response.setHeader("Content-disposition", "inline;filename=\"pic.jpg\"");
		response.setContentType("image/jpeg");
		
		
		OutputStream stream;
		try {
			stream = response.getOutputStream();

			SakaiPerson person = spm.getSakaiPerson(spm.getSystemMutableType());
			if (person == null) {
				log.warn("no system profile for user!");
				//we need to become admin
				Session session = sessionManager.getCurrentSession();
				String id = session.getUserId();
				String eid = session.getUserEid();
				person = spm.create(userDirectoryService.getCurrentUser().getId(), spm.getSystemMutableType());
				
				session.setUserId("admin");
				session.setUserEid("admin");
				
				spm.save(person);
				session.setUserId(id);
				session.setUserEid(eid);
				
				
			}
				if (person.getJpegPhoto() != null && person.getJpegPhoto().length > 0) {
					log.debug("we have some photo data");
					byte[] institutionalPhoto = person.getJpegPhoto();
					response.setContentLength(institutionalPhoto.length);
					stream.write(institutionalPhoto);
					stream.flush();
				} else {
					log.debug("no jpeg photo!");
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}




}
