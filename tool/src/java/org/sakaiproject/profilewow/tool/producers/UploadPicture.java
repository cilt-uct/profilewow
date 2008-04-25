package org.sakaiproject.profilewow.tool.producers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class UploadPicture implements ViewComponentProducer {
	
	private static Log log = LogFactory.getLog(UploadPicture.class);
	public static final String VIEW_ID ="uploadpic";
	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}

	private TargettedMessageList tml;
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.tml = tml;
	}
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		// TODO Auto-generated method stub
		
		UIForm form = UIForm.make(tofill, "upload-pic-form");
		//UIInput.make(form,"file-upload", "uploadBean")
		UICommand.make(form,"submit","uploadBean.processUpload");
		
		
		
		
	}

}
