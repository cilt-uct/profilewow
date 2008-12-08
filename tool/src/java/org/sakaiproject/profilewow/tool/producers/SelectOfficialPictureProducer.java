package org.sakaiproject.profilewow.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.profilewow.tool.params.ImageViewParamaters;
import org.sakaiproject.user.api.UserDirectoryService;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class SelectOfficialPictureProducer implements ViewComponentProducer, NavigationCaseReporter {

	public static final String VIEW_ID = "officialpic";
	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}


	private SakaiPersonManager spm;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		spm = in;
	}
	
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		// TODO Auto-generated method stub
		SakaiPerson person = spm.getSakaiPerson(spm.getSystemMutableType());
		UIInternalLink.make(tofill, "officialPic", new ImageViewParamaters("imageServlet", person.getAgentUuid() ));
		
		UIForm form = UIForm.make(tofill, "subForm");
		UICommand.make(form, "sub", UIMessage.make("useOfficialSub"),"uploadBean.useOfficial");

	}
	
	public List reportNavigationCases() {
		// TODO Auto-generated method stub
		 List<NavigationCase> togo = new ArrayList<NavigationCase> (); // Always navigate back to this view.
		 togo.add(new NavigationCase(null, new SimpleViewParameters(MainProducer.VIEW_ID)));
		return togo;
	}
}
