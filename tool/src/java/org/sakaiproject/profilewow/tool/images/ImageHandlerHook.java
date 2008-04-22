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


	public boolean handle() {
		// TODO Auto-generated method stub

		ImageViewParamaters ivp;
		if (viewparams instanceof ImageViewParamaters) {
			ivp = (ImageViewParamaters) viewparams;
			log.info("got a ImageView");
		} else {
			log.info("Not an image view!: " + viewparams);
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
		response.setHeader("Content-disposition", "inline");
		response.setContentType("image/jpeg");
		response.setHeader("filename", "pic.jpg");
		
		OutputStream stream;
		try {
			stream = response.getOutputStream();

			SakaiPerson person = spm.getSakaiPerson(spm.getSystemMutableType());
			if (person != null) {
				if (person.getJpegPhoto() != null && person.getJpegPhoto().length > 0) {
					log.info("we have some photo data");
					byte[] institutionalPhoto = person.getJpegPhoto();
					response.setContentLength(institutionalPhoto.length);
					stream.write(institutionalPhoto);
					stream.flush();
				} else {
					log.info("no jpeg photo!");
				}
			} else {
				log.warn("no profile for user!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}




}
