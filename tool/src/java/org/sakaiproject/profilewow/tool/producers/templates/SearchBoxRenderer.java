package org.sakaiproject.profilewow.tool.producers.templates;

import org.sakaiproject.profilewow.tool.params.SearchViewParamaters;
import org.sakaiproject.profilewow.tool.producers.SearchResultProducer;

import lombok.extern.slf4j.Slf4j;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;

@Slf4j
public class SearchBoxRenderer {


	
	public void renderSearchBox(UIContainer tofill, String divId) {
		//search for a profile
		log.debug("renderSearchBox(UIContainer tofill, " + divId + " )");
		UIJointContainer joint = new UIJointContainer(tofill, divId, "searchBox:");
		UIForm searchForm = UIForm.make(joint, "searchform", new SearchViewParamaters(SearchResultProducer.VIEW_ID));
		UIInput.make(searchForm, "searchin", "searchText");
		UICommand.make(searchForm, "searchsub","searchBean.search");
		log.debug("finished rendering box");
	}
	
	
}
