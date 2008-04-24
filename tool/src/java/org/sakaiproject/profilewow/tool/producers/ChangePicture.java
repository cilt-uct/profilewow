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
	
	public static final String COLLECTION_PROFILE = "Profile";
	
	/** This string used for Title of the profile collection **/
	public static final String COLLECTION_PROFILE_TITLE = "Profile Data";
	/** This string gives description for profile folder **/
	public static final String COLLECTION_PROFILE_DESCRIPTION = "Data related to the profile";
	
	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}

	
	private ContentHostingService contentHostingService;
	public void setContentHostingService(ContentHostingService contentHostingService) {
		this.contentHostingService = contentHostingService;
	}

	private SiteService siteService;

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		this.userDirectoryService = uds;
	}

	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		// TODO Auto-generated method stub
		try {
			
			ContentCollection pCollection = contentHostingService.getCollection(retrieveProfileFolderId(getCurrentUserSiteId()));
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
			
			
		} catch (PermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IdUnusedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

	
	private String getCurrentUserSiteId() {
		return siteService.getUserSiteId(userDirectoryService.getCurrentUser().getId());
	}
	
	
	
	/**
	 * Returns profile folder id using 'Profile'. If it
	 * does not exist will create it.
	 * 
	 * @param siteId
	 *            	The site to search
	 *            
	 * @return String 
	 * 				Contains the complete id for the profile folder
	 * @throws PermissionException 
	 * 
	 * @throws PermissionException
	 *             Access denied or Not found so not available
	 */
	private String retrieveProfileFolderId(String siteId) throws PermissionException {

		final String siteCollection = contentHostingService.getSiteCollection(siteId);
		String profileCollection = siteCollection + COLLECTION_PROFILE + Entity.SEPARATOR;

		try {
			contentHostingService.checkCollection(profileCollection);
			return profileCollection;

		} 
		catch (IdUnusedException e) {
			// Does not exist, so try to create it
			profileCollection = siteCollection + COLLECTION_PROFILE + Entity.SEPARATOR;
	
			createProfileFolder(profileCollection, siteId);
			return profileCollection;
			
		} 
		catch (TypeException e) {
			log.error("TypeException while getting profile folder using 'Profile' string: "
							+ e.getMessage(), e);
			throw new Error(e);
		}

	}
	
	
	/**
	 * Creates the profile folder in Resources
	 * 
	 * @param profileCollection
	 * 				The id to be used for the profile folder
	 * 
	 * @param siteId
	 * 				The site id for whom the folder is to be created
	 */
	private void createProfileFolder(String profileCollection, String siteId) {
		try {
			log.debug("createProfileFolder()");
			log.info("Could not find profile folder, attempting to create.");

			ContentCollectionEdit collection = 
						contentHostingService.addCollection(profileCollection);
			
			final ResourcePropertiesEdit resourceProperties = collection.getPropertiesEdit();
			
			resourceProperties.addProperty(ResourceProperties.PROP_DISPLAY_NAME,
											COLLECTION_PROFILE_TITLE);

			resourceProperties.addProperty(ResourceProperties.PROP_DESCRIPTION,
											COLLECTION_PROFILE_DESCRIPTION);

			contentHostingService.commitCollection(collection);

			contentHostingService.setPubView(collection.getId(), true);
		} 
		catch (Exception e) {
			// catches	IdUnusedException, 		TypeException
			//			InconsistentException,	IdUsedException
			//			IdInvalidException		PermissionException
			//			InUseException
			log.error(e.getMessage() + " while attempting to create Profile folder: "
							+ " for site: " + siteId + ". NOT CREATED... " + e.getMessage(), e);
			throw new Error(e);
		}
	}


	
	
}
