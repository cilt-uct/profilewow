package org.sakaiproject.profilewow.tool.producers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.profilewow.tool.params.SakaiPersonViewParams;
import org.sakaiproject.profilewow.tool.params.SearchViewParamaters;
import org.sakaiproject.profilewow.tool.producers.templates.SearchBoxRenderer;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class SearchResultProducer implements ViewComponentProducer,ViewParamsReporter {

	private static Log log = LogFactory.getLog(SearchResultProducer.class);
	
	public static final String VIEW_ID= "searchProfile";
	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}

	
	private SakaiPersonManager sakaiPersonManager;
	public void setSakaiPersonManager(SakaiPersonManager in) {
		sakaiPersonManager = in;
	}
	
	private TargettedMessageList messages;
	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}
	
	private SecurityService securityService;
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}
	


	private SearchBoxRenderer searchBoxRenderer;
	public void setSearchBoxRenderer(SearchBoxRenderer searchBoxRenderer) {
		this.searchBoxRenderer = searchBoxRenderer;
	}


	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		SearchViewParamaters svp = (SearchViewParamaters)viewparams;
		String searchString = svp.searchText;
		log.info("search string is: "  + searchString);
		List<SakaiPerson> profiles = this.findProfiles(searchString);
		UIMessage.make(tofill, "searchTitle", "searchTitle", new Object[]{ searchString});
		
		searchBoxRenderer.renderSearchBox(tofill, "search:");
		
		UIInternalLink.make(tofill, "mainLink", "returnToProfile",new SimpleViewParameters(MainProducer.VIEW_ID));
		for (int i =0 ; i < profiles.size(); i++) {
			SakaiPerson sPerson = (SakaiPerson) profiles.get(i);
			log.info("creating row for " + sPerson.getGivenName());
			UIBranchContainer row = UIBranchContainer.make(tofill, "resultRow:");
			String eid = null;
			try {
				eid = userDirectoryService.getUserEid(sPerson.getAgentUuid());
			} catch (UserNotDefinedException e) {
				// could be an orphaned record
				log.debug("user does not exits"  + sPerson.getAgentUuid());
				continue;
			}
			UIInternalLink.make(row, "resultLink", sPerson.getSurname() + ", " + sPerson.getGivenName(),
					new SakaiPersonViewParams(ViewProfileProducer.VIEW_ID, eid));
		}

	}

	public ViewParameters getViewParameters() {
		// TODO Auto-generated method stub
		return new SearchViewParamaters();
	}


	
	private List<SakaiPerson> findProfiles(String searchString)
	{
		if (log.isDebugEnabled())
		{
			log.debug("findProfiles(" + searchString + ")");
		}
		if (searchString == null || searchString.length() < 1)
			throw new IllegalArgumentException("Illegal searchString argument passed!");

		List profiles = sakaiPersonManager.findSakaiPerson(searchString);
		List searchResults = new ArrayList();
		SakaiPerson profile;

		if ((profiles != null) && (profiles.size() > 0))
		{
			Iterator profileIterator = profiles.iterator();

			while (profileIterator.hasNext())
			{
				profile = (SakaiPerson) profileIterator.next();

				// Select the user mutable profile for display on if the public information is viewable.
				if ((profile != null)
						&& profile.getTypeUuid().equals(sakaiPersonManager.getUserMutableType().getUuid()))
				{
					if ((getCurrentUserId().equals(profile.getAgentUuid()) || securityService.isSuperUser()))
					{
						// allow user to search and view own profile and superuser to view all profiles
						searchResults.add(profile);
					}
					else if ((profile.getHidePublicInfo() != null) && (profile.getHidePublicInfo().booleanValue() != true))
					{
						if (profile.getHidePrivateInfo() != null && profile.getHidePrivateInfo().booleanValue() != true)
						{
							searchResults.add(profile);
						}
						else
						{
							searchResults.add(getOnlyPublicProfile(profile));
						}

					}
				}

			}
		}
		log.info("found a resultsList of: " + searchResults.size());
		return searchResults;
	}

	private SakaiPerson getOnlyPublicProfile(SakaiPerson profile)
	{
		if (log.isDebugEnabled())
		{
			log.debug("getOnlyPublicProfile(Profile" + profile + ")");
		}
		profile.setJpegPhoto(null);
		profile.setPictureUrl(null);
		profile.setMail(null);
		profile.setLabeledURI(null);
		profile.setHomePhone(null);
		profile.setNotes(null);
		return profile;
	}
	
	private String getCurrentUserId() {
		return userDirectoryService.getCurrentUser().getId();
	}
	
	
	
	
	
	
	
	
}
