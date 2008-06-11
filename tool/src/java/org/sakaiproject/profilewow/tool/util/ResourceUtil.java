package org.sakaiproject.profilewow.tool.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.api.ContentCollection;
import org.sakaiproject.content.api.ContentCollectionEdit;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResourceEdit;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.exception.IdInvalidException;
import org.sakaiproject.exception.IdLengthException;
import org.sakaiproject.exception.IdUniquenessException;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.IdUsedException;
import org.sakaiproject.exception.InconsistentException;
import org.sakaiproject.exception.OverQuotaException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.ServerOverloadException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.user.api.UserDirectoryService;

public class ResourceUtil {

	
	
	public static final String COLLECTION_PROFILE = "Profile";
	
	private static Log log = LogFactory.getLog(ResourceUtil.class);
	
	/** This string used for Title of the profile collection **/
	public static final String COLLECTION_PROFILE_TITLE = "Profile Data";
	/** This string gives description for profile folder **/
	public static final String COLLECTION_PROFILE_DESCRIPTION = "Data related to the profile";
	
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
	
	
	private String getCurrentUserSiteId() {
		return siteService.getUserSiteId(userDirectoryService.getCurrentUser().getId());
	}
	
	
	
	
	public ContentCollection  getUserCollection() {
		try {
			return contentHostingService.getCollection(retrieveProfileFolderId(getCurrentUserSiteId()));
		} catch (IdUnusedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public boolean isPicture(String fileContentType) {
		if(fileContentType.compareToIgnoreCase("image/jpeg") != 0 && 
				fileContentType.compareToIgnoreCase("image/pjpeg") != 0 &&
				fileContentType.compareToIgnoreCase("image/gif") != 0 &&
				fileContentType.compareToIgnoreCase("image/png") != 0)
			return false;
		
		return true;
	}
	
	
	
	public String addPicture(String fileName, BufferedImage image, String type) {
		try {
			String folderId = retrieveProfileFolderId(getCurrentUserSiteId());
			String baseName = fileName;
			String extension = "";
			if (fileName.indexOf(".")>0) {
				baseName = fileName.substring(0, fileName.indexOf("."));
				extension = fileName.substring(fileName.indexOf(".")+1, fileName.length());
			}
				
			
			ContentResourceEdit cre = contentHostingService.addResource(folderId, baseName, extension, 5);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream( 1000 );
			ImageIO.write( image, "jpeg", baos );
			// C L O S E
			baos.flush();
			byte[] resultImageAsRawBytes = baos.toByteArray();

			baos.close();
			
			cre.setContent(resultImageAsRawBytes);
			cre.setContentType(type);
			contentHostingService.commitResource(cre);
			
			return cre.getUrl();
			
			
		} catch (PermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IdInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (ServerOverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IdUniquenessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IdLengthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IdUnusedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OverQuotaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
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
