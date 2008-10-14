package org.sakaiproject.profilewow.tool.producers.templates;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.profilewow.tool.params.SearchViewParamaters;
import org.sakaiproject.profilewow.tool.producers.SearchResultProducer;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;

public class SearchBoxRenderer {

	private static Log log = LogFactory.getLog(SearchBoxRenderer.class);
	
	public void renderSearchBox(UIContainer tofill, String divId) {
		//search for a profile
		log.info("renderSearchBox(UIContainer tofill, " + divId + " )");
		UIJointContainer joint = new UIJointContainer(tofill, divId, "searchBox:");
		UIForm searchForm = UIForm.make(joint, "searchform", new SearchViewParamaters(SearchResultProducer.VIEW_ID));
		UIInput.make(searchForm, "searchin", "searchText");
		UICommand.make(searchForm, "searchsub","searchBean.search");
		log.info("finished rendering box");
	}
	
	
}
