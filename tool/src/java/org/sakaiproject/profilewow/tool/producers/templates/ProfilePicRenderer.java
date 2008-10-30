package org.sakaiproject.profilewow.tool.producers.templates;

import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.profilewow.tool.params.ImageViewParamaters;
import org.sakaiproject.profilewow.tool.producers.ChangePicture;
import org.sakaiproject.profilewow.tool.producers.SelectOfficialPictureProducer;
import org.sakaiproject.profilewow.tool.producers.UploadPicture;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class ProfilePicRenderer {
	public static final String NO_PIC_URL = "../images/noimage.gif";
	
	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}
	
	public void makeProfilePic(UIContainer tofill, String divId, SakaiPerson sPerson) {
		
		UIJointContainer joint = new UIJointContainer(tofill,divId,"picTemplate:");
		//picture stuff
		String picUrl = sPerson.getPictureUrl();
		if (picUrl == null || picUrl.trim().length() == 0)
			picUrl = NO_PIC_URL;
		else 
			picUrl = sPerson.getPictureUrl();
		
		//The links for the upload
		UIInternalLink.make(joint,"upload-link", new SimpleViewParameters(UploadPicture.VIEW_ID));
		UIInternalLink.make(joint,"select-pic", new SimpleViewParameters(ChangePicture.VIEW_ID));
		
		//should only display if there is an official pic
		if (hasProfilePic()) {
		 UIVerbatim.make(joint, "useOf",
				 UIInternalLink.make(joint, "useOf-link", new SimpleViewParameters(SelectOfficialPictureProducer.VIEW_ID))
				 );
		}
		
		if (sPerson.isSystemPicturePreferred() != null &&  sPerson.isSystemPicturePreferred().booleanValue()) {
			UIInternalLink.make(joint, "current-pic", new ImageViewParamaters("imageServlet", sPerson.getUuid()));
		} else if (sPerson.isSystemPicturePreferred() == null || !sPerson.isSystemPicturePreferred().booleanValue() ) {
			UILink.make(joint, "current-pic", picUrl);
		}
	}

	private boolean hasProfilePic() {
		
		SakaiPerson sp = spm.getSakaiPerson(spm.getSystemMutableType());
		
		if (sp == null)
			return false;
		else if (sp.getJpegPhoto() != null)
			return true;
	
		return false;
		
	}
	
}
