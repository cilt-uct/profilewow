package org.sakaiproject.profilewow.tool.producers;


import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class ResponseMsg implements ViewComponentProducer, ViewParamsReporter {

	
	public static String VIEW_ID = "responsemsg";
	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}


	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
	}


	public ViewParameters getViewParameters() {
		// TODO Auto-generated method stub
		//return new ItemViewParameters();
		return null;
	}

}
