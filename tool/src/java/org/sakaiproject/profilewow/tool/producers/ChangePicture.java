package org.sakaiproject.profilewow.tool.producers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.api.ContentCollection;
import org.sakaiproject.content.api.ContentCollectionEdit;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.profilewow.tool.util.ResourceUtil;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.user.api.UserDirectoryService;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ChangePicture implements ViewComponentProducer {

	private static Log log = LogFactory.getLog(ChangePicture.class);
	

	public static final String VIEW_ID="changepic";
	
	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}


	private ResourceUtil resourceUtil;
	public void setResourceUtil(ResourceUtil ru) {
		resourceUtil = ru;
	}


	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		// TODO Auto-generated method stub
		
			
			ContentCollection pCollection = resourceUtil.getUserCollection();
			log.info("got a collection with " + pCollection.getMemberCount() + " objects");
			
			List resources = pCollection.getMemberResources();
			for (int i = 0; i < resources.size(); i++) {
				UIBranchContainer row = UIBranchContainer.make(tofill, "pic-row:");
				for (int q =0; q < 4 && i< resources.size(); q++) {
					ContentResource resource = (ContentResource)resources.get(i);
					UIBranchContainer cell = UIBranchContainer.make(row, "pic-cell:");
					UILink.make(cell, "pic", resource.getUrl());
					
					i++;
				}
				
				
			}
			
			
		
		
		

	}

	

	
}
